package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"path/filepath"
	"strings"
)

func main() {

	fmt.Println("xml2binding.go", len(os.Args))

	var rootPath = ""

	if len(os.Args) == 1 {
		rootPath = "/Users/taohui/DevStation/hi_android/quyuan-android/erban_client/src/main/res/layout/"
	} else {
		rootPath = os.Args[1]
	}

	if fileInfos, err := ioutil.ReadDir(rootPath); err != nil {
		fmt.Println(err.Error())
		return
	} else {
		for _, v := range fileInfos {
			targetFilePath := filepath.Join(rootPath, v.Name())
			// fmt.Println(targetFilePath)

			if fileContent, err := ioutil.ReadFile(targetFilePath); err != nil {
				fmt.Println(err.Error())
				return
			} else {
				fileContentString := string(fileContent)
				if strings.Contains(fileContentString, `<?xml version="1.0" encoding="utf-8"?>`) == false {
					fileContentString = `<?xml version="1.0" encoding="utf-8"?>` + fileContentString
				}
				if strings.Contains(fileContentString, "<layout") == false {
					newFileContentString := strings.Replace(fileContentString, `<?xml version="1.0" encoding="utf-8"?>`, `<?xml version="1.0" encoding="utf-8"?> <layout>`, 1) + "</layout>"
					fmt.Println(newFileContentString)
					ioutil.WriteFile(targetFilePath, []byte(newFileContentString), 0777)
				}
			}
		}
	}
}
