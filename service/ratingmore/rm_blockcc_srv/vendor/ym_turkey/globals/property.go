package globals

import (
	"sc_basego/globals"
	"io/ioutil"
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
		Port            string   `xml:"port,attr"`
		Env             string   `xml:"env,attr"`
		PostgresThdData Postgres `xml:"postgres_thd_data"`
		Postgres        Postgres `xml:"postgres"`
		YmDataEth       Postgres `xml:"ym_data_eth"`
		YmDataThdDB     Postgres `xml:"ym_data_thd_db"`
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
