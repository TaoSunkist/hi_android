package ch4

import (
	"errors"
	"fmt"
	"log"
	"testing"
)

var ints = []int{3, 4, 1, 2, 4, 7, 3, 6, 0, 9, 7, 8, 3, 4, 1, 5, 7, 2, 3, 8, 6, 8, 9}

func TestLabel(t *testing.T) {
	var a = 0
	goto Label1
	if a == 0 {
		goto Label2
	}
Label1:
	{
		fmt.Println(a)
	}
Label2:
	{
		fmt.Println(a)
	}
}
func TestReverseInts(t *testing.T) {
	for ints1, loop, a := make([]int, len(ints)), 0, len(ints)-1; loop < len(ints); loop, a = loop+1, a-1 {
		ints1[loop] = ints[a]
		fmt.Println(ints1[loop], ints[loop])
	}
}

func TestReverseInts2(t *testing.T) {
	for loop := 0; loop < len(ints); loop++ {
		//panic(errors.New("Fuck!!!"))
		defer fmt.Println(ints[loop])
	}
	panic(errors.New("1"))
}

func TestShowFor(t *testing.T) {
	loop1 := 0
	//TODO for迭代可以省略前：初始化子句，中：判定子句，后：逻辑计算子句
	for loop1 < 10 {
		loop1 += 2
		log.Print(loop1, ",")
	}

	loop2 := 0
	for {
		loop2 += 1
		switch {
		case loop2 > 10:
			log.Printf("%d > 10", loop2)
			goto BREAK
		}

	}
BREAK:
	{
		log.Println(loop2)
	}

	//TODO 迭代容器类
	str := "12345"
	for k, v := range str {
		switch {
		case v%2 == 0:
			log.Printf("%d: %d  %d=%d", k, v, 2, v%2)
			break
		}
	}
}
