package v1

import (
	"strings"
	"time"
)

type (
	Symbols []Symbol
	Symbol struct {
		ID        uint    `gorm:"primary_key"`
		Name      string  `json:"name" gorm:"type:varchar(128)"`
		Symbol    string  `json:"symbol" gorm:"type:varchar(16)"`
		VolumeUsd float64 `json:"volume_usd"`
		Status    string  `json:"status" gorm:"type:varchar(16)"`
		Version   int64   `json:"version" gorm:"column:version;type:integer"`
		CreatedAt time.Time
	}
)

const (
	SymbolStatus = "enable"
)

func (t *Symbols) Tag() {}

//blockcc是否支持
func (t *Symbol) IsEnable() bool { return strings.Compare(t.Status, SymbolStatus) == 0 }

func (t *Symbol) TableName() string {
	return "blockcc_symbols"
}
