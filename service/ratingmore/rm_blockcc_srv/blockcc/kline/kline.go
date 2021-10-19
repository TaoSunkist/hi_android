package kline

import (
	"net/http"
	"fmt"
	"hi_golang/tools/lop"
	"io/ioutil"
	"encoding/json"
	"hi_golang/service/ratingmore/rm_blockcc_srv/bean/blockcc"
	"time"
	"ym_turkey/globals"
	"github.com/jinzhu/gorm"
	"strings"
	"hi_golang/service/ratingmore/rm_blockcc_srv/blockcc/impl"
	"hi_golang/service/ratingmore/rm_blockcc_srv/dao"
)

const (
	Step5m      = "5m" //5分钟
	Step1d      = "1d" //1小时
	klineApiUrl = "https://data.block.cc/api/v1/kline?market=%s&symbol_pair=%s&type=%s&limit=%d"
)

type (
	KlineTask struct {
		Id                uint64
		blockccMarketPair dao.BlockccMarketPair
		Tag               string //1d,5m
		Errno             bool
		Errmsg            string
		RetryCnt          byte
		KlineDatas        KlineDatas
	}
	KlineDatas [][]float64
	KlineDaos []KlineDao
	KlineDao struct {
		UpdatedAt time.Time
		Timestamp int64  `gorm:"PRIMARY_KEY"`
		Open      float64
		Close     float64
		Low       float64
		High      float64
		Volume    float64
		Tag       string `gorm:"PRIMARY_KEY"`
	}
)

func (k *KlineTask) IsRetry() (isRetry bool, retryCnt byte, maxRetryCnt byte) { return false, k.RetryCnt, 3 }

func (*KlineDatas) Tag() {}

//根据tag计算传入的时间至今相差的数据个数
func (k *KlineTask) Limit(lastTimeStamp int64) (limit int64) {

	nowDate := time.Now()
	lastDate := time.Unix(lastTimeStamp/1000, 0)
	var diff int64
	if strings.Compare(k.Tag, Step5m) == 0 {
		diff = int64(nowDate.Sub(lastDate).Minutes())
		limit = diff / 5
		lop.T("last date time:", lastDate, ", now date time: ", time.Now(), "diff minutes: ", diff, ", limit: ", limit)
		return
	} else if strings.Compare(k.Tag, Step1d) == 0 {
		diff = int64(nowDate.Sub(lastDate).Hours())
		limit = diff / 24
		lop.T("last date time:", lastDate, ", now date time: ", time.Now(), ", diff hour: ", diff, ", limit: ", limit)
		return limit
	} else {
		return -1
	}
}

// 拉取任务需要存储的数据
func (k *KlineTask) Batch(storeCh chan<- impl.ITask) { //TODO 拉取数据

	ymDataThdDB, err := gorm.Open(globals.RunEnv.Cfg.YmDataThdDB.Driver, globals.RunEnv.Cfg.YmDataThdDB.Url)
	if err != nil {
		return
	}

	defer ymDataThdDB.Close()
	var limit int64 //控制拉取的数量
	var lastKlineDao KlineDao

	if errs := ymDataThdDB.Table(k.blockccMarketPair.PairsTableName).Order("timestamp desc").Where("tag = ?", k.Tag).Limit(1).Find(&lastKlineDao).GetErrors(); len(errs) != 0 || lastKlineDao.Timestamp == 0 {
		limit = 2000000
	} else if limit = k.Limit(lastKlineDao.Timestamp); limit == 0 {
		return
	}
	if limit == -1 {
		return
	}

	apiUrl := fmt.Sprintf(klineApiUrl, k.blockccMarketPair.MarketName, k.blockccMarketPair.SymbolPair, k.Tag, limit)
	lop.T(apiUrl, ", async Table: ", k.blockccMarketPair.PairsTableName)
	var (
		httpRespBody []byte
		result       blockcc.Result
		klineDatas   KlineDatas
	)

	if httpResp, err := http.Get(apiUrl); err != nil {
		lop.E("async blockcc api: ", apiUrl, ", errmsg: ", err)
		return
	} else if httpResp.StatusCode != 200 {
		lop.E("request blockcc api: ", apiUrl, ", response status code: ", httpResp.StatusCode)
		return
	} else if httpRespBody, err = ioutil.ReadAll(httpResp.Body); err != nil {
		lop.E("async blockcc api: ", apiUrl, ", response: ", httpResp)
		return
	} else if err = json.Unmarshal(httpRespBody, &result); err != nil {
		lop.E("async blockcc api: ", apiUrl, ", response body: ", string(httpRespBody))
		return
	} else if err := result.Unmarshal2BlockccBean(&klineDatas); err != nil {
		lop.E("async blockcc api: ", apiUrl, ", response body: ", string(httpRespBody), ", data: ", string(result.Data), ", err: ", err)
		return
	}
	if result.Code != 0 || len(klineDatas) == 0 {
		lop.E(apiUrl, "result : ", result, ", async Table: ", k.blockccMarketPair.PairsTableName, ", row size: ", len(klineDatas))
		return
	}
	k.KlineDatas = klineDatas
	storeCh <- k

}

func (*KlineTask) RunWait() time.Duration { return time.Minute * 15 }

// 落地任务产出
func (k *KlineTask) Store() {
	klineDatas := k.KlineDatas
	//lop.T("store data: ", k.MarketName, k.SymbolPair, k.Tag)
	lop.T(k.blockccMarketPair.MarketName, k.blockccMarketPair.SymbolPair, ", async Table: ", k.blockccMarketPair.PairsTableName, ", row size: ", len(klineDatas))
	ymDataThdDB, err := gorm.Open(globals.RunEnv.Cfg.YmDataThdDB.Driver, globals.RunEnv.Cfg.YmDataThdDB.Url)
	if err != nil {
		//TODO 出错处理
		return
	}

	defer ymDataThdDB.Close()
	for _, klineData := range klineDatas {
		klineDao := KlineDao{Timestamp: int64(klineData[0]), Open: klineData[1], Close: klineData[2], Low: klineData[3], High: klineData[4], Volume: klineData[5], Tag: k.Tag}
		if errs := ymDataThdDB.Table(k.blockccMarketPair.PairsTableName).Create(&klineDao).GetErrors(); len(errs) == 0 {
			lop.I("by ", k.blockccMarketPair.MarketName, k.blockccMarketPair.SymbolPair, ", fetch table: ", k.blockccMarketPair.PairsTableName, " successfully")
		} else { //TODO 一旦出错是否将后面队列的数据全部跳过?
			lop.E("by ", k.blockccMarketPair.MarketName, k.blockccMarketPair.SymbolPair, ", fetch table: ", k.blockccMarketPair.PairsTableName, " failure: ", errs)
		}
	}
	var count int64
	ymDataThdDB.Table(k.blockccMarketPair.PairsTableName).Count(&count)
	//同步更新记录条目数到
	if errs := ymDataThdDB.Debug().Model(&(k.blockccMarketPair)).Update("rows_cnt", count).GetErrors(); len(errs) != 0 {
		lop.E(errs)
	}

	lop.T("update blockcc_market_pairs table rows_cnt = ", len(k.KlineDatas), " successfully.")
}

// 构建任务
func SyncKline(batchCh chan<- impl.ITask) {
	YmDataThdDB, err := gorm.Open(globals.RunEnv.Cfg.YmDataThdDB.Driver, globals.RunEnv.Cfg.YmDataThdDB.Url)
	if err != nil {
		lop.E(err)
		return
	}
	defer YmDataThdDB.Close()
	var blockccMarketPairs dao.BlockccMarketPairs
	if errs := YmDataThdDB.Find(&blockccMarketPairs).GetErrors(); len(errs) != 0 {
		lop.E(errs)
	}
	for _, v := range blockccMarketPairs {
		if YmDataThdDB.HasTable(v.TableName) {
			lop.I("PairsTableName: ", v.TableName, " created.")
		} else if errs := YmDataThdDB.Debug().Table(v.PairsTableName).CreateTable(&KlineDao{}).GetErrors(); len(errs) != 0 {
			lop.W("create table: ", v.TableName, ", errmsg: ", errs)
		}
		batchCh <- &KlineTask{blockccMarketPair: v, RetryCnt: 0, Tag: Step1d}
		batchCh <- &KlineTask{blockccMarketPair: v, RetryCnt: 0, Tag: Step5m}
		time.Sleep(4 * time.Second)
	}
}
