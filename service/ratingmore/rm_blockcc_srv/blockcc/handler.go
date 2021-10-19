package blockcc

import (
	"hi_golang/service/ratingmore/rm_blockcc_srv/blockcc/impl"
	"hi_golang/service/ratingmore/rm_blockcc_srv/blockcc/kline"
	"net/http"
	_ "net/http/pprof"
	"time"
)

var (
	transport = &http.Transport{
		MaxIdleConns:       100,              //控制主机最大空闲连接数量,0意味着没有限制
		IdleConnTimeout:    60 * time.Second, //空闲连接超时关闭的时间
		DisableCompression: true,
	}
)

// 每次取三个
var batchCh = make(chan impl.ITask, 1)
var storeCh = make(chan impl.ITask, 10)

func Initialize() {
	//go func() {
	//	for {
	//		market_pairs.SyncMarketParis(batchCh)
	//		time.Sleep(1 * time.Hour)
	//	}
	//}()

	go func() {
		for {
			kline.SyncKline(batchCh)
		}
	}()

	//go func() { exrate.SyncExrates(batchCh) }()
	for {
		select {
		case batchTask := <-batchCh: // 启动任务拉取数据
			go batchTask.Batch(storeCh)
		case storeTask := <-storeCh: // 存储任务的数据
			go storeTask.Store()
		}
	}
}
