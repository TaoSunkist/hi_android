package ch3

import (
	"fmt"
	"testing"
)

type A struct {
	name string
}

func TestNew1(t *testing.T) {
	var interface1 = new(interface{})
	fmt.Println(fmt.Sprintf("%v", interface1), &interface1)
	var slice1 = new([]string)
	var slice2 = make([]string, 0)
	//输出有差异
	var slice2Ptr = &slice2
	fmt.Println(slice1, slice2, slice2Ptr)
	var a = A{name: "July"}
	var a2 = struct {
		name string
	}{name: "July"}
	//a2的潜在类型
	fmt.Println(a == a2, &a, &a2)
}
