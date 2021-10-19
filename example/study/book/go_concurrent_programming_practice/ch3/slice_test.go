package ch3

import (
	. "fmt"
	"testing"
)

type Test2 struct {
	name string
}

func TestSlice1(t *testing.T) {

	var slice1 = []string{8: "Go", 2: "Python", "Java", "C", "C++", "PHP"}
	Println(Sprintf("%v", slice1), len(slice1), cap(slice1))

	slice1 = append(slice1, "Ruby", "Erlang")
	Println(Sprintf("%v", slice1), len(slice1), cap(slice1))

	for k := range slice1 {
		Println("slice1[", k, "]:", slice1[k], ",", &slice1[k])
	}
	//内存地址值相同
	for slice1_1, loop := slice1[5:], 0; loop < len(slice1_1); loop++ {
		Println("slice1[", loop, "]:", slice1_1[loop], ",", &slice1_1[loop])
	}
	//*取地址，&取值
	//值类型和引用类型的创建方式
	var a = new(int64)
	Println(*a)
	var ints = new([10]int)
	Println(ints[2])

	slice1 = append(slice1, "Ruby", "Erlang")
	Println(cap(slice1))
	var slice2 = []string{8: "Go", 2: "Python", "Java", "C", "C++", "PHP"}
	//Ref类型只能使用nil进行比较和值类型不同
	Println(slice2 == nil)
	var test Test2 = Test2{name: ""}
	Println(test == Test2{})
}

func TestSlice2(t *testing.T) {
	var slice1 = []int{0, 1, 2, 3, 4, 5, 6}
	Println(Sprintf("slice = %v, len = %d, cap = %d", slice1, len(slice1), cap(slice1)))
	Println(slice1[0:5:5], len(slice1[3:5:5]), cap(slice1[3:5:5]))
	var slice2 = []int{0,1,2}
	var i = len(slice2)
	Println(slice1[0:i-1])
}
func TestSlice3(t *testing.T) {
	var slice1 = []string{8: "Go", 2: "Python", "Java", "C", "C++", "PHP"}
	Println(cap(slice1), len(slice1))
	slice1 = append(slice1, "Ruby", "Erlang")
	Println(Sprintf("%v", slice1))
	Println(len(slice1))
	slice1 = append(slice1, "Ruby", "Erlang")
	Println(cap(slice1))
	var slice2 = []string{8: "Go", 2: "Python", "Java", "C", "C++", "PHP"}
	//Ref类型只能使用nil进行比较和值类型不同
	Println(slice2 == nil)
	var test Test2 = Test2{name: ""}
	Println(test == Test2{})
}
