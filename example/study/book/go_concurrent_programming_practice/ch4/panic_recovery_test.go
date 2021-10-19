package ch4

import (
	"testing"
	"log"
	"github.com/pkg/errors"
)

func TestPanicRecovery(t *testing.T) {
	defer func(function interface{}) {
		var a = function.(func() int)
		log.Println("----->", a())
	}(func() int {
		log.Print("argFun1")
		return 1
	})
}

func TestPanicRecovery1(t *testing.T) {
	defer func() { t.Log(recover()) }()
	panic(errors.New("aaaa"))
	t.Log(1 / 1)
}
