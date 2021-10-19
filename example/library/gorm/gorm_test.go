package gorm

import (
	"fmt"
	"github.com/jinzhu/gorm"
	_ "github.com/lib/pq"
	"log"
	"testing"
	"time"
)

var TableTags = []string{
	"rpt_population_active_seq",
	"rpt_active_ad_count",
	"rpt_revenue_index",
	"rpt_industrial_structure_index",
	"rpt_rank_coin_cnt",
	"rpt_new_dormant_addr",
	"rpt_addr_type_status",
	"rpt_month_liucun",
	"rpt_fresh_add",
	"rpt_transaction_group_cnt",
	"rpt_trans_active_time_spread",
	"rpt_revenue_gas_used_pk",
	"rpt_day_aver_miner_fee",
	"rpt_miner_block_aver_monthly",
	"rpt_all_net_suanli",
	"rpt_token_addr_analy",
	"rpt_token_trans_cnt",
	"rpt_token_trans_monthly",
	"rpt_token_avg_trans_fee",
	"rpt_token_add_fresh",
	"rpt_group_coin_sort",
	"rpt_coin_participate_sort",
}

func TestGorm(t *testing.T) {
	tableNameCh := make(chan string)
	getTableNameFunc := func(tableTag string) {
		gormDB, err := gorm.Open("postgres", "host=datasource.com port=9700 user=postgres dbname=ym_data_eth password=postgres_BTC sslmode=disable")
		if err != nil {
			log.Println(err)
			return
		}
		defer gormDB.Close()
		var tableName struct{ TableTag string }
		if errs := gormDB.Debug().Select("table_tag").Where("table_name = ?", tableTag).Table("rpt_overview").Scan(&tableName).GetErrors(); len(errs) != 0 {
			log.Println(errs)
			return
		}
		tableNameCh <- fmt.Sprintf("%s: %s", tableName, tableTag)
	}
	go func() {
		for loop := 0; ; loop++ {
			go getTableNameFunc(TableTags[loop])
			if loop == len(TableTags)-1 {
				loop = 0
				time.Sleep(10 * time.Millisecond)
			}
		}
	}()
	for {
		select {
		case tableInfo := <-tableNameCh:
			{
				log.Println(tableInfo)
			}
		}
	}
}
