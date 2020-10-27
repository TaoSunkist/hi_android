package main

import (
	"bytes"
	"fmt"
	"github.com/antchfx/htmlquery"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jinzhu/gorm"
	"golang.org/x/net/html"
	"hi_golang/tools/lop"
	"io/ioutil"
	"net/http"
	"strings"
)

const (
	wallpagerHtmlTag = `//a[@href]`
	pageSize         = 23
	currentPageIndex = 1
)

/*
https://wallhaven.cc/toplist
wallhaven-caught-result-DB-Table
*/
type Wallpage struct {
	gorm.Model
	/* seconds */
	HrefUrl     string `json:"href_url"`
	OfPageIndex uint32 `json:"of_page_index"`
	IsGet       bool   `json:"is_get"`
	ImageID     string `json:"image_id"`
	FromWebsite string `json:"from_website"`
}

func (w Wallpage) TableName() string {
	return `wallpage`
}

var (
	db            *gorm.DB
	err           error
	wallpagesChan chan []Wallpage
)

func init() {

	db, err = gorm.Open("mysql", "root:Taohui520MARIADB!@#@(35.186.152.49)/wwwthsunkist?charset=utf8&parseTime=True&loc=Local")
	panic(err)
}

func main() {

	lop.T("running")
	lop.D("running")
	lop.I("running")
	lop.W("running")
	lop.E("running")

	go func() {

		if request, err := http.NewRequest("GET",

			fmt.Sprintf("https://wallhaven.cc/toplist?page=%d",
				currentPageIndex), nil); err != nil {
			lop.E(err)
		} else {

			httpCli := http.Client{}
			response, err := httpCli.Do(request)

			if err != nil {
				lop.E(err)
				return
			}

			responseBodyBytes, err := ioutil.ReadAll(response.Body)
			responseBodyBytesReader := bytes.NewReader(responseBodyBytes)
			doc, err := htmlquery.Parse(responseBodyBytesReader)

			if err != nil {
				lop.E(err)
			} else {
				parseResponse(doc, wallpagesChan)
			}
		}
	}()

	select {
	case wallpages := <-wallpagesChan:
		{
			downloadWallpages(wallpages)
		}
	}
}

func downloadWallpages(wallpages []Wallpage) {
	/* 如果无法下载了, 那么就需要通知上游, 停止拉取 */
}

/* 获取抓取目录的列表/ */
func parseResponse(doc *html.Node, wallpageChan chan<- []Wallpage) {

	if list := htmlquery.Find(doc, wallpagerHtmlTag); len(list) == 0 {
		return
	} else {

		var wallpages = make([]Wallpage, pageSize)
		for _, v := range list {

			hrefUrl := htmlquery.SelectAttr(v, "href")
			if len(v.Attr) < 1 || v.Attr[0].Val != "preview" {
				continue
			}
			var hrefAttrs = strings.SplitAfter(hrefUrl, "/")
			imageID := hrefAttrs[len(hrefAttrs)-1]

			var wallpage = Wallpage{
				ImageID:     imageID,
				HrefUrl:     hrefUrl,
				OfPageIndex: currentPageIndex,
				FromWebsite: strings.ReplaceAll(hrefAttrs[2], `/`, ``),
			}

			wallpages = append(wallpages, wallpage)
			lop.T(wallpage)
		}
		wallpageChan <- wallpages
		return
	}
}
