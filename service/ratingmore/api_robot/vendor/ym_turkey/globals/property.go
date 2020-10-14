package globals

import (
	"io/ioutil"
	"sc_basego/globals"
	"hi_golang/tools/lop"
	"encoding/xml"
	"strings"
)

type (
	srvCfg struct {
		Env  string `xml:"env,attr"`
		Cfgs []Cfg  `xml:"cfg"`
	}
	Cfg struct {
		Port             string   `xml:"port,attr"`
		PprofPort        string   `xml:"pprof_port"`
		Env              string   `xml:"env,attr"`
		PostgresDataThd  Postgres `xml:"postgres_thd_data"`
		PostgresDataAddr Postgres `xml:"postgres_addr_data"`
		Postgres         Postgres `xml:"postgres"`
		YmDataEth        Postgres `xml:"ym_data_eth"`
		YmDataBtc        Postgres `xml:"ym_data_btc"`
		//YmDataBtc3       Postgres `xml:"ym_data_btc3"`
		YmDataBtc22 Postgres `xml:"ym_data_btc22"`
		YmDataBtc6  Postgres `xml:"ym_data_btc6"`
		//YmDataBtc7       Postgres `xml:"ym_data_btc7"`
		YmDataBtc9  Postgres `xml:"ym_data_btc9"`
		YmDataEth2  Postgres `xml:"ym_data_eth2"`
		EthGraphDb  Postgres `xml:"eth_graph_db"`
		MysqlScVelk Postgres `xml:"mysql_sc_velk"`
		Spk01Rds    Postgres `xml:"spk01_rds"`

		Redis struct {
			Alias string `xml:"alias,attr"`
			Url   string `xml:"url,attr"`
		} `xml:"redis"`
	}
	Postgres struct {
		CommonCfg
	}
	CommonCfg struct {
		Url    string `xml:"url,attr"`
		Driver string `xml:"driver,attr"`
		Alias  string `xml:"alias,attr"`
	}
)

const (
	PROD = "prod"
	SIM  = "sim"
	DEV  = "dev"
)

type Configure struct {
	Env string
	Cfg *Cfg
}

var RunEnv *Configure

func init() {
	var s srvCfg
	fileBytes, err := ioutil.ReadFile(globals.RootPath + "/conf/config.xml")
	if err != nil {
		lop.E(err)
		panic(err)
	} else if err := xml.Unmarshal(fileBytes, &s); err != nil {
		lop.E(err)
		panic(err)
	}
	for _, v := range s.Cfgs {
		if strings.Compare(v.Env, s.Env) == 0 {
			RunEnv = &Configure{s.Env, &v}
			break
		}
	}
}
