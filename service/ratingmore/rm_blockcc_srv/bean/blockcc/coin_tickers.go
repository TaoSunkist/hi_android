package blockcc

import "time"

// https://block.cc/api/v1/coin/tickers?coin=eos&exchange=&symbol=&page=&size=max
type (
	TickersResult struct {
		Tickers   Tickers             `json:"tickers"`
		Page      int                 `json:"page"`
		Size      int                 `json:"size"`
		PageCount int                 `json:"page_count"`
		CompareTo func(i, j int) bool `json:"-"`
	}

	Tickers2Result struct {
		Page       int                 `json:"page"`
		Size       int                 `json:"size"`
		TotalPage  int                 `json:"total_page"`
		TotalCount int                 `json:"total_count"`
		Tickers    Tickers2            `json:"list"`
		CompareTo  func(i, j int) bool `json:"-"`
	}
	Tickers2 []Ticker2

	Ticker2 struct {
		Timestamps  int64   `json:"timestamps"`
		Last        float64 `json:"last"`
		High        float64 `json:"high"`
		Low         float64 `json:"low"`
		Bid         float64 `json:"bid"`
		Ask         float64 `json:"ask"`
		Vol         float64 `json:"vol"`
		BaseVolume  float64 `json:"base_volume"`
		ChangeDaily float64 `json:"change_daily"`
		Market      string  `json:"market"`
		SymbolName  string  `json:"symbol_name"`
		SymbolPair  string  `json:"symbol_pair"`
		Rating      int     `json:"rating"`
		HasKline    bool    `json:"has_kline"`
		UsdRate     float64 `json:"usd_rate"`
		Version     int64   `json:"-"`
	}

	Tickers []Ticker
	Ticker struct {
		RID                 int64   `json:"id" gorm:"column:id;primary_key;auto_increment"`
		ID                  string  `json:"_id" gorm:"type:char(24);column:_id"`
		DisplayPairName     string  `json:"display_pair_name" gorm:"type:varchar(128)"`
		Price               float64 `json:"price"`
		Volume              float64 `json:"volume"`
		EnableKline         bool    `json:"enableKline"`
		Timestamps          int64   `json:"timestamps"`
		DataCenterPairName  string  `json:"dataCenter_pair_name"`
		URL                 string  `json:"url"`
		ExchangeName        string  `json:"exchange_name"`
		ExchangeDisplayName string  `json:"exchange_display_name"`
		Status              string  `json:"status"`
		CoinID              string  `json:"coin_id" gorm:"column:coin_id"`
		CoinName            string  `json:"coin_name"`
		CoinSymbol          string  `json:"coin_symbol"`
		ExchangeZhName      string  `json:"exchange_zh_name"`
		NativePrice         float32 `json:"native_price"`
		High1D              float64 `json:"high1d"`
		Low1D               float64 `json:"low1d"`
		BaseSymbol          string  `json:"base_symbol"`
		Change1D            float64 `json:"change1d"`
		Type                int     `json:"type"`
		Bid                 float64 `json:"bid"`
		Ask                 float64 `json:"ask"`
		Percent             float64 `json:"percent"`
		Version             int64   `json:"-"`
		//如果存在如下三个字段则是 hard-delete-mode，否则是soft-delete-mode
		CreatedAt time.Time
		//UpdatedAt           time.Time
		//DeletedAt           *time.Time `sql:"index"`
	}
)

func (t *TickersResult) Tag()               {}
func (t *TickersResult) Len() int           { return len(t.Tickers) }
func (t *TickersResult) Less(i, j int) bool { return t.CompareTo(i, j) }
func (t *TickersResult) Swap(i, j int)      { t.Tickers[i], t.Tickers[j] = t.Tickers[j], t.Tickers[i] }
func (t *Ticker) TableName() string         { return "test_tickers" }

func (t *Tickers2Result) Len() int           { return len(t.Tickers) }
func (t *Tickers2Result) Less(i, j int) bool { return t.CompareTo(i, j) }
func (t *Tickers2Result) Swap(i, j int)      { t.Tickers[i], t.Tickers[j] = t.Tickers[j], t.Tickers[i] }
func (t *Tickers2Result) Tag()               {}
func (t *Ticker2) TableName() string         { return "blockcc_tickers" }

//--------------------------------------------------------------------------------
