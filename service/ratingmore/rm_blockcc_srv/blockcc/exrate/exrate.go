package exrate

import (
	"net/http"
	"fmt"
	"hi_golang/tools/lop"
	"io/ioutil"
	"time"
	"hi_golang/service/ratingmore/rm_blockcc_srv/blockcc/impl"
	"github.com/bitly/go-simplejson"
	"ym_turkey/service/redigo"
)

type (
	ExrateDatas string
	ExrateTask struct {
		MarketName  string
		SymbolPair  string
		TableName   string
		Tag         string //1d,5m
		Errno       bool
		Errmsg      string
		RetryCnt    byte
		ExrateDatas ExrateDatas
	}
)

const EXRATES = "EXRATES"

func (k *ExrateTask) IsRetry() (isRetry bool, retryCnt byte, maxRetryCnt byte) { return false, k.RetryCnt, 3 }

func (*ExrateDatas) Tag() {}

var transport = &http.Transport{
	MaxIdleConns:       100,              //控制主机最大空闲连接数量,0意味着没有限制
	IdleConnTimeout:    60 * time.Second, //空闲连接超时关闭的时间
	DisableCompression: true,
}

// 拉取任务需要存储的数据
func (k *ExrateTask) Batch(storeCh chan<- impl.ITask) { //TODO 拉取数据

	httpCli := &http.Client{Transport: transport}
	symbolUrl := fmt.Sprintf("https://data.block.cc/api/v1/exrate?base=CNY")

	req, err := http.NewRequest("GET", symbolUrl, nil)
	if err != nil {
		lop.E(err)
	}

	resp, err := httpCli.Do(req)
	if err != nil {
		lop.E(err)
		return
	} else if resp.StatusCode != 200 {
		lop.E("request: ", symbolUrl, " error:", resp.StatusCode)
		return
	}

	bodyBytes, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		lop.E("read response body error: ", err)
		return
	}
	bodyJson, err := simplejson.NewJson(bodyBytes)
	if err != nil {
		lop.E("parse response body error: ", err)
	}

	dataJson := bodyJson.Get("data")

	if dataJsonString, err := func() (string, error) {
		dataJsonBytes, err := dataJson.Encode()
		dataJsonString := string(dataJsonBytes)
		return dataJsonString, err
	}(); err != nil {
		lop.E(err)
	} else if err := redigo.Mset(60*5, false, EXRATES, dataJsonString); err != nil {
		lop.E("cache data write to redis key: "+EXRATES, ", values: ", "... ... occur error: "+err.Error())
	} else {
		//lop.I("cache data write to redis key: "+EXRATES, ", values: ", len(dataJsonString), "... ... successfully.")
	}
	dataJsonStr, err := dataJson.String()
	if err != nil {
		return
	}
	k.ExrateDatas = ExrateDatas(dataJsonStr)
	storeCh <- k

}

func (*ExrateTask) RunWait() time.Duration { return time.Minute * 15 }

// 落地任务产出
func (k *ExrateTask) Store() {
	if err := redigo.Mset(0, false, k.ExrateDatas); err != nil {
		lop.E("save blockcc exrates failure: ", err)
	} else {
		//lop.I("save blockcc exrates successfully")
	}
}

// 构建任务
func SyncExrates(batchCh chan<- impl.ITask) {
	for {
		batchCh <- &ExrateTask{}
		time.Sleep(time.Minute)
	}
}
