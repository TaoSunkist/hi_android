package ch3

import (
	"log"
	"strconv"
	"testing"
)

func TestPtr(t *testing.T) {
	tenString := new(TenString)
	for loop := len(tenString) - 1; loop > 0; loop-- {
		tenString[loop] = strconv.Itoa(loop)
	}
	log.Println(tenString)
	log.Println(*tenString)
	log.Println(&tenString)
	tenString.strArr()
	log.Println(tenString)
	tenString.strArr2()
	log.Println(tenString)
}

type TenString [10]string

func (t *TenString) strArr() {
	t[2] = "9"
}
func (t TenString) strArr2() {
	t[3] = "9"
}
