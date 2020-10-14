package api_cache_robot

type INewScheme interface {
	GetAddr() string
	GetTokenaddr() string
}
type addr2TokenaddrsMap map[string][]string



func (a *addr2TokenaddrsMap) Put(scheme INewScheme) {
	addr := scheme.GetAddr()
	tokenaddr := scheme.GetTokenaddr()
	if _, ok := (*a)[addr]; ok {
		(*a)[addr] = append((*a)[addr], tokenaddr)
	} else {
		(*a)[addr] = []string{tokenaddr}
	}
}

func Puts(scheme INewScheme) {
	scheme.GetAddr()
	scheme.GetTokenaddr()
}

func (a *addr2TokenaddrsMap) InitGraphTokenAddr() {
	for k, tokenAddrs := range *a { //填充默认展示图形的请求token地址
		(*a)[k] = append(tokenAddrs, "")
	}
}

