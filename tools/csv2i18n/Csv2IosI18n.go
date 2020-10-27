package csv2i18n

import (
	"bufio"
	"encoding/csv"
	"fmt"
	"io"
	"log"
	"os"
	"regexp"
	"strings"
	"sync/atomic"
)

func main() {
	csvFilePath := "/Users/taohui/DevStation/hi_golang/src/hi_golang/tools/csv/Hilo翻译_V2.0.3.csv"
	Csv2IOSI18n(csvFilePath)
	Csv2AndroidI18n(csvFilePath)
}

func Csv2AndroidI18n(csvFilePath string) {
	csvFile, _ := os.Open(csvFilePath)
	reader := csv.NewReader(bufio.NewReader(csvFile))
	reader.LazyQuotes = true
	//var people []Person
	androidI18nFiles := make(map[string][]string, 13)
	/* 汉语 */
	androidI18nFiles["zh"] = []string{}
	/* 英语 */
	androidI18nFiles["en"] = []string{}
	/* 阿语 */
	androidI18nFiles["ar"] = []string{}
	/* 土耳其语 */
	androidI18nFiles["tr"] = []string{}
	/* 印尼语 */
	androidI18nFiles["id"] = []string{}
	/* 俄语 */
	androidI18nFiles["ru"] = []string{}
	/* 韩语 */
	androidI18nFiles["ko"] = []string{}
	/* 葡萄牙语 */
	androidI18nFiles["pt"] = []string{}
	/* 泰语 */
	androidI18nFiles["th"] = []string{}
	/* 西班牙语 */
	androidI18nFiles["es"] = []string{}
	/* 印地语 */
	androidI18nFiles["hi"] = []string{}
	/* 越南语 */
	androidI18nFiles["vi"] = []string{}
	/* 乌尔都语 */
	androidI18nFiles["ur"] = []string{}

	firstBool := atomic.NewBool(true)
	for {
		line, error := reader.Read()
		if error == io.EOF {
			break
		} else if error != nil {
			log.Fatal(error)
		}
		if firstBool.Swap(false) == true {
			continue
		}
		/* 汉语 */
		androidI18nFiles["zh"] = append(androidI18nFiles["zh"], line[1])
		/* 英语 */
		androidI18nFiles["en"] = append(androidI18nFiles["en"], line[2])
		/* 阿语 */
		androidI18nFiles["ar"] = append(androidI18nFiles["ar"], line[3])
		/* 土耳其语 */
		androidI18nFiles["tr"] = append(androidI18nFiles["tr"], line[4])
		/* 印尼语 */
		androidI18nFiles["id"] = append(androidI18nFiles["id"], line[5])
		/* 俄语 */
		androidI18nFiles["ru"] = append(androidI18nFiles["ru"], line[6])
		/* 韩语 */
		androidI18nFiles["ko"] = append(androidI18nFiles["ko"], line[7])
		/* 葡萄牙语 */
		androidI18nFiles["pt"] = append(androidI18nFiles["pt"], line[8])
		/* 泰语 */
		androidI18nFiles["th"] = append(androidI18nFiles["th"], line[9])
		/* 西班牙语 */
		androidI18nFiles["es"] = append(androidI18nFiles["es"], line[10])
		/* 印地语 */
		androidI18nFiles["hi"] = append(androidI18nFiles["hi"], line[11])
		/* 越南语 */
		androidI18nFiles["vi"] = append(androidI18nFiles["vi"], line[12])
		/* 乌尔都语 */
		androidI18nFiles["ur"] = append(androidI18nFiles["ur"], line[13])
	}

	re := regexp.MustCompile("\\w{2,}")

	/* initialize keys */
	var keys []string
	var keyDuplicateMap = make(map[string]uint32)
	for _, v := range androidI18nFiles["en"] {
		upperString := strings.ToLower(v)
		upperString = strings.ReplaceAll(upperString, "’", "")
		upperString = strings.ReplaceAll(upperString, "'", "")
		upperString = strings.ReplaceAll(upperString, " ", "_")
		upperString = re.FindString(upperString)
		if duplicateID, isExist := keyDuplicateMap[upperString]; isExist {
			keyDuplicateMap[upperString] = duplicateID + 1
			keys = append(keys, fmt.Sprint(upperString, keyDuplicateMap[upperString]))
		} else {
			keyDuplicateMap[upperString] = 0
			keys = append(keys, upperString)
		}
	}

	for k, androidI18nFileValues := range androidI18nFiles {
		mkDirAllResultErr := os.MkdirAll("values-"+k, 0777)
		if mkDirAllResultErr != nil {
			log.Fatal(mkDirAllResultErr)
		}
		stringsPwd := "values-" + k + "/strings.xml"
		androidI18nFile, err := os.OpenFile(stringsPwd, os.O_CREATE|os.O_RDWR, 0777)
		if err != nil {
			log.Fatal(err)
		}

		if _, err := androidI18nFile.WriteString("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n"); err != nil {
			log.Fatal(err)
		}
		for k, v := range keys {
			if len(v) == 0 {
				break
			}
			var androidI18nFileValue = androidI18nFileValues[k]
			androidI18nFileValue = strings.ReplaceAll(androidI18nFileValue, "\n", `\n`)
			itemString := fmt.Sprint(`<string name="`, v, `">`, androidI18nFileValue, `</string>`, "\n")
			itemString = strings.ReplaceAll(itemString, `'`, `\'`)
			var count = strings.Count(itemString, "%s")
			for i := 0; i < count; i++ {
				itemString = strings.Replace(itemString, "%s", fmt.Sprintf("%%%d$s", i+1), 1)
			}
			androidI18nFile.WriteString(itemString)
		}
		androidI18nFile.WriteString("\n</resources>")
		androidI18nFile.Close()
	}
	fmt.Println("operated was successfully. ")
}

func Csv2IOSI18n(csvFilePath string) {
	csvFile, _ := os.Open(csvFilePath)
	reader := csv.NewReader(bufio.NewReader(csvFile))
	reader.LazyQuotes = true
	//var people []Person
	androidI18nFiles := make(map[string][]string, 13)
	/* 汉语 */
	androidI18nFiles["zh"] = []string{}
	/* 英语 */
	androidI18nFiles["en"] = []string{}
	/* 阿语 */
	androidI18nFiles["ar"] = []string{}
	/* 土耳其语 */
	androidI18nFiles["tr"] = []string{}
	/* 印尼语 */
	androidI18nFiles["id"] = []string{}
	/* 俄语 */
	androidI18nFiles["ru"] = []string{}
	/* 韩语 */
	androidI18nFiles["ko"] = []string{}
	/* 葡萄牙语 */
	androidI18nFiles["pt"] = []string{}
	/* 泰语 */
	androidI18nFiles["th"] = []string{}
	/* 西班牙语 */
	androidI18nFiles["es"] = []string{}
	/* 印地语 */
	androidI18nFiles["hi"] = []string{}
	/* 越南语 */
	androidI18nFiles["vi"] = []string{}
	/* 乌尔都语 */
	androidI18nFiles["ur"] = []string{}

	firstBool := atomic.NewBool(true)
	for {
		line, error := reader.Read()
		if error == io.EOF {
			break
		} else if error != nil {
			log.Fatal(error)
		}
		if firstBool.Swap(false) == true {
			continue
		}
		/* 汉语 */
		androidI18nFiles["zh"] = append(androidI18nFiles["zh"], line[1])
		/* 英语 */
		androidI18nFiles["en"] = append(androidI18nFiles["en"], line[2])
		/* 阿语 */
		androidI18nFiles["ar"] = append(androidI18nFiles["ar"], line[3])
		/* 土耳其语 */
		androidI18nFiles["tr"] = append(androidI18nFiles["tr"], line[4])
		/* 印尼语 */
		androidI18nFiles["id"] = append(androidI18nFiles["id"], line[5])
		/* 俄语 */
		androidI18nFiles["ru"] = append(androidI18nFiles["ru"], line[6])
		/* 韩语 */
		androidI18nFiles["ko"] = append(androidI18nFiles["ko"], line[7])
		/* 葡萄牙语 */
		androidI18nFiles["pt"] = append(androidI18nFiles["pt"], line[8])
		/* 泰语 */
		androidI18nFiles["th"] = append(androidI18nFiles["th"], line[9])
		/* 西班牙语 */
		androidI18nFiles["es"] = append(androidI18nFiles["es"], line[10])
		/* 印地语 */
		androidI18nFiles["hi"] = append(androidI18nFiles["hi"], line[11])
		/* 越南语 */
		androidI18nFiles["vi"] = append(androidI18nFiles["vi"], line[12])
		/* 乌尔都语 */
		androidI18nFiles["ur"] = append(androidI18nFiles["ur"], line[13])
	}

	//re := regexp.MustCompile("\\w{2,}")

	/* initialize keys */
	var keys []string
	var keyDuplicateMap = make(map[string]uint32)
	for _, v := range androidI18nFiles["en"] {
		upperString := v
		//upperString = strings.ReplaceAll(upperString, "’", "")
		upperString = strings.ReplaceAll(upperString, "\n", "")
		upperString = strings.ReplaceAll(upperString, " ", "_")
		//upperString = re.FindString(upperString)
		if duplicateID, isExist := keyDuplicateMap[upperString]; isExist {
			keyDuplicateMap[upperString] = duplicateID + 1
			keys = append(keys, fmt.Sprint(upperString, keyDuplicateMap[upperString]))
		} else {
			keyDuplicateMap[upperString] = 0
			keys = append(keys, upperString)
		}
	}

	for k, androidI18nFileValues := range androidI18nFiles {
		mkDirAllResultErr := os.MkdirAll("values-"+k, 0777)
		if mkDirAllResultErr != nil {
			log.Fatal(mkDirAllResultErr)
		}
		stringsPwd := "values-" + k + "/strings.xml"
		androidI18nFile, err := os.OpenFile(stringsPwd, os.O_CREATE|os.O_RDWR, 0777)
		if err != nil {
			log.Fatal(err)
		}

		if _, err := androidI18nFile.WriteString(""); err != nil {
			log.Fatal(err)
		}
		for k, v := range keys {
			if len(v) == 0 {
				break
			}
			var androidI18nFileValue = androidI18nFileValues[k]
			//androidI18nFileValue = strings.ReplaceAll(androidI18nFileValue, "\n", `\n`)
			itemString := fmt.Sprint(`"`, v, `"="`, androidI18nFileValue, `";`, "\n")
			itemString = strings.ReplaceAll(itemString, `'`, `\'`)
			itemString = strings.ReplaceAll(itemString, "%s", "%@")
			androidI18nFile.WriteString(itemString)
		}
		androidI18nFile.Close()
	}
	fmt.Println("operated was successfully. ")
}
