package dao

import "time"

const (
	TbEthEtherscanBlockccs  = "eth_etherscan_blockcc"
	TbBlockccMarketPairs    = "blockcc_market_pairs"
	TbEthContractTokenTypes = "eth_contract_token_types"
)

type (
	EthEtherscanBlockccs []EthEtherscanBlockcc
	EthEtherscanBlockcc struct {
		Id               int64
		Erc20Contract    string
		TokenName        string
		BlockccTokenName string
		CoinType         string
	}
	EthContractTokenTypes []EthContractTokenType
	EthContractTokenType struct {
		Id            int    `gorm:"column:id,primary key"`
		CoinType      string
		Erc20Contract string `gorm:"column:erc20_contract"`
	}
	BlockccMarketPairs []BlockccMarketPair
	BlockccMarketPair struct {
		ID                uint64 `gorm:"PRIMARY_KEY;AUTO_INCREMENT"`
		MarketName        string
		DisplayMarketName string
		SymbolPair        string `gorm:"type:varchar(64)"`
		Erc20             string
		RowsCnt           int64
		CoinSymbol        string `gorm:"type:varchar(64)"`
		CoinName          string `gorm:"type:varchar(128)"`
		PairsTableName    string `grom:"type:varchar(256)"`
		CreatedAt         time.Time
	}
)

func (*EthEtherscanBlockccs) TableName() string    { return TbEthEtherscanBlockccs }
func (e *EthContractTokenTypes) TableName() string { return TbEthContractTokenTypes }
func (e *EthContractTokenType) TableName() string  { return TbEthContractTokenTypes }
func (b *BlockccMarketPairs) TableName() string    { return TbBlockccMarketPairs }
func (b *BlockccMarketPair) TableName() string     { return TbBlockccMarketPairs }
func (b *BlockccMarketPairs) Tag()                 {}
