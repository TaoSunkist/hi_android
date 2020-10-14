package impl

type CfgSyncer interface {
	Fetch(dstCh chan<- CfgSyncer)
	Store(oriCh chan<- CfgSyncer)
}
