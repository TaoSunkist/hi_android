package main

import (
	"bufio"
	"encoding/csv"
	"fmt"
	"hi_golang/tools/lop"
	"io"
	"log"
	"os"
	"regexp"
	"strings"

	"go.uber.org/atomic"
)

func main() {
	// csvFilePath := os.Args[1]
	// outputLanguage := os.Args[2]
	// fmt.Println(csvFilePath)
	// outputLanguages := strings.Split(outputLanguage, ",")
	// for i := 0; i < len(outputLanguages); i++ {
	// 	fmt.Println(outputLanguages[i])
	// }
	// Csv2AndroidI18n2(csvFilePath, outputLanguages)
	csvFilePath := "/Users/taohui/DevStation/hi_golang/src/hi_golang/tools/csv2i18n/csv/Lami Live翻译 - V1.0.0.csv"
	Csv2AndroidI18n(csvFilePath)
}

func Csv2AndroidI18n(csvFilePath string) {
	csvFile, _ := os.Open(csvFilePath)
	reader := csv.NewReader(bufio.NewReader(csvFile))
	reader.LazyQuotes = true
	//var people []Person
	androidI18nFiles := make(map[string][]string, 5)
	/* 汉语 */
	androidI18nFiles["zh"] = []string{}
	/* 英语 */
	androidI18nFiles["en"] = []string{}
	/* 印尼语 */
	androidI18nFiles["id"] = []string{}
	/* 西班牙语 */
	androidI18nFiles["es"] = []string{}
	/* 阿语 */
	androidI18nFiles["ar"] = []string{}
	/* 土耳其语
	androidI18nFiles["tr"] = []string{}*/
	/* 印尼语
	androidI18nFiles["id"] = []string{}*/
	/* 俄语
	androidI18nFiles["ru"] = []string{}*/
	/* 韩语
	androidI18nFiles["ko"] = []string{} */
	/* 葡萄牙语
	androidI18nFiles["pt"] = []string{}*/
	/* 泰语
	androidI18nFiles["th"] = []string{} */
	/* 印地语
	androidI18nFiles["hi"] = []string{}*/
	/* 越南语
	androidI18nFiles["vi"] = []string{}*/
	/* 乌尔都语
	androidI18nFiles["ur"] = []string{} */

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
		androidI18nFiles["zh"] = append(androidI18nFiles["zh"], line[0])
		/* 英语 */
		androidI18nFiles["en"] = append(androidI18nFiles["en"], line[1])
		/* 印尼语 */
		androidI18nFiles["id"] = append(androidI18nFiles["id"], line[2])
		/* 西班牙语 */
		androidI18nFiles["es"] = append(androidI18nFiles["es"], line[3])
		/* 阿语 */
		androidI18nFiles["ar"] = append(androidI18nFiles["ar"], line[4])
		/* 土耳其语
		androidI18nFiles["tr"] = append(androidI18nFiles["tr"], line[4])*/
		/* 俄语
		androidI18nFiles["ru"] = append(androidI18nFiles["ru"], line[6])*/
		/* 韩语
		androidI18nFiles["ko"] = append(androidI18nFiles["ko"], line[7])*/
		/* 葡萄牙语
		androidI18nFiles["pt"] = append(androidI18nFiles["pt"], line[8])*/
		/* 泰语
		androidI18nFiles["th"] = append(androidI18nFiles["th"], line[9])*/
		/* 印地语
		androidI18nFiles["hi"] = append(androidI18nFiles["hi"], line[11])*/
		/* 越南语
		androidI18nFiles["vi"] = append(androidI18nFiles["vi"], line[12])*/
		/* 乌尔都语
		androidI18nFiles["ur"] = append(androidI18nFiles["ur"], line[13])*/
	}
	fmt.Println(androidI18nFiles)
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
		upperString = "s_" + upperString
		/* 如果是纯数字 */

		if duplicateID, isExist := keyDuplicateMap[upperString]; isExist {
			keyDuplicateMap[upperString] = duplicateID + 1
			keys = append(keys, "empty_"+fmt.Sprint(upperString, keyDuplicateMap[upperString]))
		} else {
			keyDuplicateMap[upperString] = 0
			keys = append(keys, upperString)
		}
	}

	/** 国家的简写 to 文件名 */
	for countryShortName, androidI18nFileValues := range androidI18nFiles {
		mkDirAllResultErr := os.MkdirAll("values-"+countryShortName, 0777)
		if mkDirAllResultErr != nil {
			log.Fatal(mkDirAllResultErr)
		}
		stringsPwd := "values-" + countryShortName + "/strings.xml"
		androidI18nFile, err := os.OpenFile(stringsPwd, os.O_CREATE|os.O_RDWR, 0777)
		if err != nil {
			log.Fatal(err)
		}

		/** 生成xml文件头部声明 */
		if _, err := androidI18nFile.WriteString("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n"); err != nil {
			log.Fatal(err)
		}

		for k, content := range keys {
			if len(content) == 0 {
				continue
			}
			fmt.Println("taohui", content)

			var androidI18nFileValue = androidI18nFileValues[k]
			androidI18nFileValue = strings.ReplaceAll(androidI18nFileValue, "\n", `\n`)
			itemString := fmt.Sprint(`<string name="`, content, `">`, androidI18nFileValue, `</string>`, "\n")
			itemString = strings.ReplaceAll(itemString, `'`, `\'`)

			/** 为了处理一些特殊语言在输入的时候会自动顺序颠倒的问题 */
			if (strings.Compare("ar", countryShortName) == 0 || strings.Compare("ur", countryShortName) == 0) && strings.Contains(itemString, "s%") {
				lop.I(countryShortName, itemString)
				itemString = strings.ReplaceAll(itemString, "s%", "%s")
			}

			var count = strings.Count(itemString, "%s")
			for i := 0; i < count; i++ {
				itemString = strings.Replace(itemString, "%s", fmt.Sprintf("%%%d$s", i+1), 1)
			}

			/** 处理百分号，%符号影响格式化的操作 */
			var percentRegex = regexp.MustCompile("\\d+(?:\\.\\d+)?%")
			var percentRegexResults = percentRegex.FindAllString(itemString, len(itemString))

			if len(percentRegexResults) > 0 {
				for i := 0; i < len(percentRegexResults); i++ {
					var percentRegexResult = percentRegexResults[i]
					var count = strings.Count(itemString, percentRegexResults[i])
					for i := 0; i < count; i++ {
						itemString = strings.Replace(itemString, percentRegexResult, percentRegexResult+"%", 1)
					}
				}
			}
			androidI18nFile.WriteString(itemString)
		}

		androidI18nFile.WriteString("\n</resources>")
		androidI18nFile.Close()
	}
	fmt.Println("operated was successfully. ")
}

func Csv2AndroidI18nV2(csvFilePath string, outputLanguages []string) {

	csvFile, _ := os.Open(csvFilePath)
	reader := csv.NewReader(bufio.NewReader(csvFile))
	reader.LazyQuotes = true
	androidI18nFiles := make(map[string][]string, len(outputLanguages))

	for {
		firstBool := atomic.NewBool(true)
		line, error := reader.Read()
		if error == io.EOF {
		}
		if firstBool.Swap(false) == true {
			continue
		}

		for index := 0; index < len(outputLanguages); index++ {
			elem := outputLanguages[index]
			androidI18nFiles[elem] = append(androidI18nFiles[elem], line[index])
		}
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

	/** 国家的简写 to 文件名 */
	for countryShortName, androidI18nFileValues := range androidI18nFiles {
		mkDirAllResultErr := os.MkdirAll("values-"+countryShortName, 0777)
		if mkDirAllResultErr != nil {
			log.Fatal(mkDirAllResultErr)
		}
		stringsPwd := "values-" + countryShortName + "/strings.xml"
		androidI18nFile, err := os.OpenFile(stringsPwd, os.O_CREATE|os.O_RDWR, 0777)
		if err != nil {
			log.Fatal(err)
		}

		/** 生成xml文件头部声明 */
		if _, err := androidI18nFile.WriteString("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n"); err != nil {
			log.Fatal(err)
		}

		for k, content := range keys {
			fmt.Println(countryShortName, androidI18nFileValues)

			if len(content) == 0 {
				break
			}

			var androidI18nFileValue = androidI18nFileValues[k]
			androidI18nFileValue = strings.ReplaceAll(androidI18nFileValue, "\n", `\n`)
			itemString := fmt.Sprint(`<string name="`, content, `">`, androidI18nFileValue, `</string>`, "\n")
			itemString = strings.ReplaceAll(itemString, `'`, `\'`)

			/** 为了处理一些特殊语言在输入的时候会自动顺序颠倒的问题 */
			if (strings.Compare("ar", countryShortName) == 0 || strings.Compare("ur", countryShortName) == 0) && strings.Contains(itemString, "s%") {
				lop.I(countryShortName, itemString)
				itemString = strings.ReplaceAll(itemString, "s%", "%s")
			}

			var count = strings.Count(itemString, "%s")
			for i := 0; i < count; i++ {
				itemString = strings.Replace(itemString, "%s", fmt.Sprintf("%%%d$s", i+1), 1)
			}

			/** 处理百分号，%符号影响格式化的操作 */
			var percentRegex = regexp.MustCompile("\\d+(?:\\.\\d+)?%")
			var percentRegexResults = percentRegex.FindAllString(itemString, len(itemString))

			if len(percentRegexResults) > 0 {
				for i := 0; i < len(percentRegexResults); i++ {
					var percentRegexResult = percentRegexResults[i]
					var count = strings.Count(itemString, percentRegexResults[i])
					for i := 0; i < count; i++ {
						itemString = strings.Replace(itemString, percentRegexResult, percentRegexResult+"%", 1)
					}
				}
			}
			androidI18nFile.WriteString(itemString)
		}

		androidI18nFile.WriteString("\n</resources>")
		androidI18nFile.Close()
	}
	fmt.Println("operated was successfully. ")
}
