package api_cache_robot

import (
	"gorm.io/gorm"
	"hi_golang/tools/lop"
	"net/http"
	"ym_turkey/globals"
	"ym_turkey/impl"
)

//TODO 准备物料, 从sc_add_tag中获取数据
/*
http://127.0.0.1:9011/v1/graph/addr/get_trade_diversion: 桑基图:无法实现
addr
toAddr
tokenAddr
transValue
time

X-LOCALE
http://127.0.0.1:9011/v1/graph/addr/get_trade_top_list
addr
tokenAddr:空和非空

http://127.0.0.1:9011/v1/graph/addr/get_balance_vary_dist
addr
tokenAddr
tag:Normal|History|Current

http://127.0.0.1:9011/v1/graph/addr/get_balance_history_price
addr
tokenAddr

http://127.0.0.1:9011/v1/graph/personal/get_balance_dist
addr

http://127.0.0.1:9011/v1/graph/personal/get_token_trade_list
addr

task list: select address,add_name from sc_add_tag where length(add_name) != 0;
 */
type RequestMetadata struct {
	apiUrl     string
	httpHeader http.Header
}

type apiCacheRobot struct {
	requests []*http.Request
}

var ApiCacheRobotPtr = new(apiCacheRobot)

const (
	getTradeTopList        = "http://turkey.searchain.io:%s/v1/graph/addr/get_trade_top_list"
	getBalanceVaryDist     = "http://turkey.searchain.io:%s/v1/graph/addr/get_balance_vary_dist"
	getBalanceHistoryPrice = "http://turkey.searchain.io:%s/v1/graph/addr/get_balance_history_price"
	getBalanceDist         = "http://turkey.searchain.io:%s/v1/graph/personal/get_balance_dist"
	getTokenTradeList      = "http://turkey.searchain.io:%s/v1/graph/personal/get_token_trade_list"
)

var getBalanceVaryDistTagParams = []string{"Normal", "History", "Current"}

func (a *apiCacheRobot) Fetch(dstCh chan<- impl.CfgSyncer) {
	scVelkDB, err := gorm.Open(globals.RunEnv.Cfg.MysqlScVelk.Driver, globals.RunEnv.Cfg.MysqlScVelk.Url)
	var (
		scAddTags            model.ScAddTags
		enCns                = []string{"en", "" /*cn*/ }
		addrs                []string
		addrStatus           eth.AddrStatus
		addr2TokenaddrMapPtr = make(addr2TokenaddrsMap)
		httpRequests         []*http.Request
	)

	if err != nil {
		lop.E(err)
		goto End
	}
	defer scVelkDB.Close()

	if errs := scVelkDB.Where("is_exchange != 0").Find(&scAddTags).GetErrors(); len(errs) != 0 {
		lop.E(errs)
		goto End
	}

	addrs = scAddTags.GetAddrs()
	addrStatus.SelectByAddrs(addrs) //查询addr_status_new表
	for _, addrStatu := range addrStatus {
		addr2TokenaddrMapPtr.Put(addrStatu)
	}
	/*初始化图形默认的请求数据格式,这里应该要去掉, 这里是为了第一次计算图形数据时出现过慢的问题*/
	addr2TokenaddrMapPtr.InitGraphTokenAddr()
	for _, locale := range enCns {
		for addr, tokenAddrs := range addr2TokenaddrMapPtr {
			for _, tokenAddr := range tokenAddrs {
				httpHeader := http.Header{}
				httpHeader.Set("locale", locale)
				httpHeader.Set("addr", addr)
				httpHeader.Set("tokenAddr", tokenAddr)
				getTradeTopListReq, _ := http.NewRequest("GET", fmt.Sprintf(getTradeTopList, globals.RunEnv.Cfg.Port), nil)
				getTradeTopListReq.Header.Set("addr", addr, )
				getTradeTopListReq.Header.Set("tokenAddr", tokenAddr)
				getTradeTopListReq.Header.Set("X-LOCALE", locale)
				httpRequests = append(httpRequests, getTradeTopListReq)

				getBalanceHistoryPriceReq, _ := http.NewRequest("GET", fmt.Sprintf(getBalanceHistoryPrice, globals.RunEnv.Cfg.Port), nil)
				getBalanceHistoryPriceReq.Header = getTradeTopListReq.Header
				httpRequests = append(httpRequests, getBalanceHistoryPriceReq)

				getBalanceDistReq, _ := http.NewRequest("GET", fmt.Sprintf(getBalanceDist, globals.RunEnv.Cfg.Port), nil)
				getBalanceDistReq.Header = getTradeTopListReq.Header
				httpRequests = append(httpRequests, getBalanceDistReq)

				getTokenTradeListReq, _ := http.NewRequest("GET", fmt.Sprintf(getTokenTradeList, globals.RunEnv.Cfg.Port), nil)
				getTokenTradeListReq.Header = getTradeTopListReq.Header
				httpRequests = append(httpRequests, getTokenTradeListReq)

				getBalanceVaryDistReq, _ := http.NewRequest("GET", fmt.Sprintf(getBalanceVaryDist, globals.RunEnv.Cfg.Port), nil)
				getBalanceVaryDistReq.Header.Set("addr", addr, )
				getBalanceVaryDistReq.Header.Set("tokenAddr", tokenAddr)
				getBalanceVaryDistReq.Header.Set("X-LOCALE", locale)
				for _, v := range getBalanceVaryDistTagParams {
					getBalanceVaryDistReq.Header.Set("tag", v)
					httpRequests = append(httpRequests, getTradeTopListReq)
				}
			}
		}
	}
	a.requests = httpRequests
End:
	{
		dstCh <- a
	}
}

func (a *apiCacheRobot) Store(oriCh chan<- impl.CfgSyncer) {
	httpCli := http.Client{}
	for _, request := range a.requests {
		st := time.Now()
		if resp, e := httpCli.Do(request); e != nil {
			lop.E(e)
		} else if resp.StatusCode != 200 {
			lop.E(resp)
		} else {
			bodyBts, e := ioutil.ReadAll(resp.Body)
			if len(string(bodyBts)) > 32 {
				lop.T(request.URL, ": ", ", Header: ", request.Header, ", Response: ", string(bodyBts)[:31], ", duration(sec):", time.Now().Sub(st))
			} else {
				lop.T(request.URL, ": ", ", Header: ", request.Header, ", Response: ", ", duration(sec):", time.Now().Sub(st), ", e: ", e)
			}
		}
	}
	oriCh <- a
}
