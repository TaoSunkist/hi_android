package faq

import (
	"fmt"
	"testing"
)

//使用并发的闭包时可能会出现一些混淆。考虑下面的程序：
func TestClosuresAndGoroutines(t *testing.T) {
	var done = make(chan bool)
	var strs = []string{"a", "b", "c", "d", "e"}
	for _, v := range strs { //这不是重新声明
		//fmt.Println(&v)
		go func(u string) {
			fmt.Println(&u)
			done <- true
		}(v)
	}
	for _ = range strs {
		fmt.Println(<-done)
	}
}
