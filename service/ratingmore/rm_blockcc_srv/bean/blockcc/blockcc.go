package blockcc

import (
	"encoding/json"
)

type (
	Result struct {
		Code    int             `json:"code"`
		Message string          `json:"message"`
		Data    json.RawMessage `json:"data"`
		Bean    IBean
	}
	IBean interface{ Tag() }
)

func (r *Result) Unmarshal2BlockccBean(iBean IBean) error { return json.Unmarshal(r.Data, iBean) }
