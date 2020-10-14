package api_cache_robot

import (
	"ym_turkey/impl"
)

var cfgSyncers = []impl.CfgSyncer{
	ApiCacheRobotPtr, // Api缓存
}

func init() {
	for _, v := range cfgSyncers { //启动
		oriCh <- v
	}
	func() {
		for {
			select {
			case ori := <-oriCh:
				go ori.Fetch(dstCh)
				break
			case dst := <-dstCh:
				go dst.Store(oriCh)
				break
			}
		}
	}()

}

var oriCh = make(chan impl.CfgSyncer, 10)
var dstCh = make(chan impl.CfgSyncer, 10)
