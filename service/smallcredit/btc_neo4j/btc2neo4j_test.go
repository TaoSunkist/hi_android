package btc_neo4j

import (
	"io/ioutil"
	"hi_golang/tools/lop"
	"fmt"
	"os"
	"encoding/json"
	"strings"
	"bytes"
	"io"
	"time"
	"hi_golang/tools/neo4j"
	"testing"
)

type config struct {
	CsvPaths       []string `json:"csv_paths"`
	LoadCsvCqls    []string `json:"load_csv_cqls"`
	HTTPAddr       string   `json:"http_addr"`
	BoltAddr       string   `json:"bolt_addr"`
	BatchNum       int      `json:"batch_num"`
	CompleteSuffix string   `json:"complete_suffix"`
}

func TestBtc2neo4j(t *testing.T) {
	var conf config
	if configJsonBytes, err := ioutil.ReadFile("./config.json"); err != nil {
		return
	} else if err := json.Unmarshal(configJsonBytes, &conf); err != nil {
		lop.E(err)
		return
	}
	//loadTwoCsvFiles2Neo4j(conf)
	loadOneCsvFiles2Neo4j(conf, 1)
}

var oneCh chan int

func doTasks(tasks []os.FileInfo) {
	lop.I(fmt.Sprint("tasks size: ", len(tasks)))
	time.Sleep(2 * time.Second)
	oneCh <- 1
}

func loadTwoCsvFiles2Neo4j(conf config) {
	if csvFileInfos, err := ioutil.ReadDir(conf.CsvPaths[0]); err != nil {
		lop.E(err)
		return
	} else {

		var neo4jStatementCql string
		for _, csvFileInfo := range csvFileInfos {
			neo4jJober := neo4j.New()
			if strings.HasSuffix(csvFileInfo.Name(), conf.CompleteSuffix) { //如果是已经完成的
				continue
			}
			csvFilePath := fmt.Sprintf("file:%s/%s", conf.CsvPaths[0], csvFileInfo.Name())
			neo4jStatementCql = fmt.Sprintf(conf.LoadCsvCqls[0], csvFilePath)
			lop.I(neo4jStatementCql)
			if result, err := neo4jJober.AddStatement(neo4jStatementCql).AddParam(nil).Exec(conf.HTTPAddr); err != nil || len(result.Errors) != 0 {
				lop.E(fmt.Sprint("import to neo4j error:", err, csvFilePath, ",", result))
				continue
			} else {
				lop.I(fmt.Sprint(csvFilePath, " import success."))
			}
			go completed(csvFilePath)
		}
	}
}

//完成
func completed(csvFilePath string) {
	var outputBuf0 bytes.Buffer
	csvFile, _ := os.Open(csvFilePath)
	defer csvFile.Close()
	for {
		tempOutput := make([]byte, 5120)
		n, err := csvFile.Read(tempOutput)
		//如果命令的output值的长度，那么变量n代表命令输出的字节数量
		//否则n等于output0的值的长度
		if err != nil {
			if err == io.EOF {
				break
			} else {
				break
			}
		}
		if n > 0 {
			outputBuf0.Write(tempOutput)
		}
	}
	ioutil.WriteFile(csvFilePath+".completed", outputBuf0.Bytes(), 0666)
	os.Remove(csvFilePath)
}

//	var conf config
//	if configBytes, err := ioutil.ReadFile("./config.json"); err != nil {
//		lop.E(err)
//		return
//	} else {
//		json.Unmarshal(configBytes, &conf)
//	}
//	for index := range conf.CsvPaths {
//		csvPath := conf.CsvPaths[index]
//		csvCql := conf.LoadCsvCqls[index]
//		httpAddr := conf.HTTPAddr
//		go loadCsv2Neo4j(csvPath, csvCql, httpAddr)
//	}
//}
//
//func loadCsv2Neo4j(csvPath, csvCql, httpAddr string) {
//	ioutil.ReadDir(csvCql)

func loadOneCsvFiles2Neo4j(conf config, index int) {
	csvFileInfos, err := ioutil.ReadDir(conf.CsvPaths[index])
	csvFileInfosLen := len(csvFileInfos)
	if err != nil || csvFileInfosLen == 0 {
		lop.E(err)
		return
	}

	lop.T(fmt.Sprintf("csv file num: %d", csvFileInfosLen))
	taskNum := csvFileInfosLen / conf.BatchNum
	lop.T(fmt.Sprint("Batch num:", conf.BatchNum, ", Single Batch size:", taskNum))

	//如果任务总数量<BatchNum，则直接执行
	if conf.BatchNum < csvFileInfosLen {
		doTasks(csvFileInfos)
		return
	}

	//如果任务总数量>BatchNum，开始分块
	oneCh = make(chan int, conf.BatchNum)
	for loop := 0; loop < conf.BatchNum; loop++ {
		var tasks []os.FileInfo
		//切割任务
		if loop == conf.BatchNum-1 { //末尾的任务数量可能会比之前的多出几个
			tasks = csvFileInfos[loop*taskNum : csvFileInfosLen]
		} else {
			tasks = csvFileInfos[loop*taskNum : (loop+1)*taskNum]
		}
		go doTasks(tasks)
	}
}
