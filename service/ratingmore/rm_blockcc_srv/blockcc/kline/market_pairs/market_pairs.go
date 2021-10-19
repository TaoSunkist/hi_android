package market_pairs

import (
	"hi_golang/service/ratingmore/rm_blockcc_srv/blockcc/impl"
	"encoding/json"
	"hi_golang/tools/lop"
	"hi_golang/service/ratingmore/rm_blockcc_srv/dao"
	"strings"
	"fmt"
	"time"
	"github.com/jinzhu/gorm"
	"ym_turkey/globals"
	"net/http"
	"io/ioutil"
	"hi_golang/service/ratingmore/rm_blockcc_srv/bean/blockcc"
)

type (
	MarketPairs []MarketPair
	SymbolPairs []string
	MarketPair struct {
		Name        string      `json:"name"`
		DisplayName string      `json:"display_name"`
		SymbolPairs SymbolPairs `json:"symbol_pairs"`
	}
	PairNames []string
	MarketPairsTask struct {
		WaitDuration       *time.Duration
		Errno              bool
		Errmsg             string
		RetryCnt           byte
		blockccMarketPairs dao.BlockccMarketPairs
	}
)

var (
	pairNames      = PairNames{"BTC", "USDT", "ETH"}
	marketNamesStr = "bitmex_binance_okex_huobipro_bitfinex_upbit_coinsbank_bibox_gdax"
)

func (*MarketPairsTask) RunWait() time.Duration {
	return time.Hour
}

func SyncMarketParis(batchCh chan<- impl.ITask) {
	batchCh <- &MarketPairsTask{}
}
func (m *MarketPairsTask) Batch(storeCh chan<- impl.ITask) {
	//lop.I("starting async blockcc market paris data.")
	ymDataThdDB, err := gorm.Open(globals.RunEnv.Cfg.YmDataThdDB.Driver, globals.RunEnv.Cfg.YmDataThdDB.Url)
	if err != nil {
		lop.E(err)
		return
	}
	defer ymDataThdDB.Close()
	var blockccMarketPair dao.BlockccMarketPair
	if ymDataThdDB.Limit(1).Order("created_at").Find(&blockccMarketPair); blockccMarketPair.ID != 0 &&
		time.Now().Sub(blockccMarketPair.CreatedAt).Minutes() < 60.0 {
		//lop.T("sync")
		return
	}

	//TODO 获取上一次创建的时间
	var result blockcc.Result
	var marketPairs MarketPairs
	if resp, err := http.Get("https://data.block.cc/api/v1/market_pairs"); err != nil {
		lop.E(err)
		return
	} else if respBodyBytes, err := ioutil.ReadAll(resp.Body); err != nil {
		lop.E(resp.StatusCode, err)
		return
	} else if err := json.Unmarshal(respBodyBytes, &result); err != nil {
		lop.E(err)
		return
	} else if err := result.Unmarshal2BlockccBean(&marketPairs); err != nil {
		lop.E(err)
		return
	}

	var ethEtherscanBlockccs dao.EthEtherscanBlockccs
	if err != nil {
		lop.E(err)
	} else if errs := ymDataThdDB.Find(&ethEtherscanBlockccs).GetErrors(); len(errs) != 0 {
		lop.E(errs)
	}
	var blockccMarketPairs dao.BlockccMarketPairs
	//TODO 不想改了😢
	for _, marketPair := range marketPairs { //遍历blockcc给出的各个交易所支持的交易对
		if strings.Contains(marketNamesStr, marketPair.Name) { //判断blockcc给出的交易所是不是我们需要的十个中的
			for _, symbolPairName := range marketPair.SymbolPairs {
				for _, pairName := range pairNames {
					if strings.HasSuffix(symbolPairName, pairName) {
						if len(ethEtherscanBlockccs) != 0 {
							for _, ethEtherscanBlockcc := range ethEtherscanBlockccs {
								if len(ethEtherscanBlockcc.Erc20Contract) != 0 && strings.HasPrefix(strings.ToLower(symbolPairName), strings.ToLower(ethEtherscanBlockcc.CoinType+"_")) {
									blockccMarketPair := dao.BlockccMarketPair{
										MarketName:        marketPair.Name,
										DisplayMarketName: marketPair.DisplayName,
										CoinSymbol:        ethEtherscanBlockcc.CoinType,
										Erc20:             ethEtherscanBlockcc.Erc20Contract,
										CoinName:          ethEtherscanBlockcc.BlockccTokenName,
										SymbolPair:        symbolPairName,
										PairsTableName:    fmt.Sprintf("blockccex_%s_%s", marketPair.Name, strings.ToLower(symbolPairName)),
									}
									blockccMarketPairs = append(blockccMarketPairs, blockccMarketPair)
								}
							}
						}
					} else {
						continue
					}
				}
			}
		}
	}
	m.blockccMarketPairs = blockccMarketPairs
	storeCh <- m

}
func (m *MarketPairsTask) Store() {
	ymDataThdDB, err := gorm.Open(globals.RunEnv.Cfg.YmDataThdDB.Driver, globals.RunEnv.Cfg.YmDataThdDB.Url)
	if err != nil {
		lop.E(err)
		return
	}
	defer ymDataThdDB.Close()
	var blockccMarketPair dao.BlockccMarketPair
	if ymDataThdDB.Limit(1).Order("created_at").Find(&blockccMarketPair); blockccMarketPair.ID != 0 &&
		time.Now().Sub(blockccMarketPair.CreatedAt).Minutes() < 60.0 {
		//lop.T("sync")
		return
	}
	blockccMarketPairs := m.blockccMarketPairs
	lop.E(len(blockccMarketPairs))
	ymDataThdDB.Debug().CreateTable(blockccMarketPairs)

	tx := ymDataThdDB.Begin()
	tx.Delete(blockccMarketPairs)
	for index, blockccMarketPair := range blockccMarketPairs {
		if errs := tx.Debug().Create(&blockccMarketPair).GetErrors(); len(errs) != 0 {
			lop.E(index, blockccMarketPair)
			continue
		}
	}
	tx.Commit()
}
func (k *MarketPairsTask) IsRetry() (isRetry bool, retryCnt byte, maxRetryCnt byte) { return false, k.RetryCnt, 3 }

func (*MarketPairs) Tag() {}
