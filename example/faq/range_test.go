package faq

import (
	"fmt"
	"testing"
)

/*
为什么这个range会导致切片的遍历出现异常
*/
func TestFaq(t *testing.T) {

	var array = []int{3, 2, 4}
	var arrayLen = len(array)

	for i := range array {
		for i1 := range array[i:arrayLen] {
			fmt.Println(array[i], array[i1])
		}
	}

	fmt.Println()

	/*  */
	for i := 0; i < arrayLen; i++ {
		for i1 := i; i1 < arrayLen; i1++ {
			fmt.Println(array[i], array[i1])
		}
	}
}
