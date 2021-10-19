package simplejson

import (
	"testing"
	"net/http"
	"io/ioutil"
	"github.com/bitly/go-simplejson"
	"encoding/json"
	"log"
)

func BenchmarkSimplejson1(b *testing.B) {
	b.StopTimer()
	resp, err := http.Get("https://data.block.cc/api/v1/price?symbol=BTC,ETH")
	if err != nil {
		b.Fatal(err)
	} else if resp.StatusCode != 200 {
		b.Fatal(resp)
	}
	respBodyBytes, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		b.Fatal(err)
	}
	respBodyJson, err := simplejson.NewJson(respBodyBytes)
	if err != nil {
		b.Fatal(err)
	}
	dataJsonBytes, err := json.Marshal(respBodyJson.Get("data"))
	b.Log(string(dataJsonBytes))
	respBodyArr, err := respBodyJson.Get("data").Array()
	if err != nil {
		b.Fatal(err)
	}
	for _, elem := range respBodyArr {
		if elemMap, ok := elem.(map[string]interface{}); ok {
			log.Println(elemMap)
		} else {
			//TODO 错误处理
		}
	}

	dataJson := respBodyJson.Get("data")
	b.StartTimer()
	b.Log(dataJson.Encode())
}
