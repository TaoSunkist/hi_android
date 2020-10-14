package main

import (
	"fmt"
	"log"
	"net/http"
)

func main() {
	fmt.Println("1111")
	httpHeader := http.Header{}
	httpHeader.Set("locale", "")
	httpHeader.Set("addr", "")

	if request, err := http.NewRequest("GET", "https://wallhaven.cc/toplist", nil); err != nil {
		log.Fatal(err)
	} else {
		httpCli := http.Client{}
		if response, err := httpCli.Do(request); err != nil {
			log.Fatal(err)
		} else {
			processResponse(response)
		}
	}

}

func processResponse(response *http.Response) {

}
