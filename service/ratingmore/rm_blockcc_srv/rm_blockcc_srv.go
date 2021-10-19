package main

import (
	"fmt"
	_ "ym_turkey/globals"
	"hi_golang/service/ratingmore/rm_blockcc_srv/blockcc"
)

type Task func()

const (
	SYMBOLS = "symbols"
)

func main() {
	fmt.Println("Owl")
	blockcc.Initialize()
}
