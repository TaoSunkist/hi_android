package impl

import "time"

type (
	ITask interface {
		Batch(storeCh chan<- ITask)                               //从Blockcc拉取数据
		IsRetry() (isRetry bool, retryCnt byte, maxRetryCnt byte) //是否重试,已经重试的次数,重试的最大次数
		Store()                                                   //存储拉取的数据
		RunWait() time.Duration
	}
	ITaskData interface {
		Tag()
	}
)
