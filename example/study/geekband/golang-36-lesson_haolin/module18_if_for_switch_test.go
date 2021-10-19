package golang36lessonhaolin

import (
	"fmt"
	"testing"
)

func TestIfForSwitch(t *testing.T) {
	numbers1 := []int{1, 2, 3, 4, 5, 6}
	/* range关键字右边的numbers1会先被求值。 */
	for i := range numbers1 {
		if i == 3 {
			numbers1[i] *= i
		}
	}
	fmt.Println(numbers1)
}

func TestIfForSwitch2(t *testing.T) {

	numbers2 := [...]int{1, 2, 3, 4, 5, 6}
	maxIndex2 := len(numbers2) - 1

	for i, e := range numbers2 {
		if i == maxIndex2 {

			/* 判断是否迭代到尾部, 推算的计算结果应该是: numbers2[0]=1+6=7 */
			numbers2[0] += e
		} else {

			/*
				numbers2[1] = 2 + 1 = 3
				numbers2[2] = 3 + 2 = 5
				numbers2[3] = 4 + 3 = 7
				numbers2[3] = 5 + 4 = 9
			*/
			numbers2[i+1] += e
		}
	}
	fmt.Println(numbers2)
}

/*
range表达式只会在for语句开始执行时被求值一次，无论后边会有多少次迭代；
range表达式的求值结果会被复制，也就是说，被迭代的对象是range表达式结果值的副本而不是原值。
*/

func TestIfForSwitch3(t *testing.T) {

	numbers2 := append(make([]int, 0), 1, 2, 3, 4, 5, 6)
	fmt.Println(numbers2)
	maxIndex2 := len(numbers2) - 1

	for i, e := range numbers2 {

		if i == maxIndex2 {

			numbers2[0] += e
		} else {

			/*
				因为切片是引用类型, 所以, 每次对numbers2的修改都会影响我们的下一次计算
					numbers2[1] = 2 + 1 = 3
					numbers2[2] = 3 + 2 = 5
					numbers2[3] = 4 + 3 = 7
					numbers2[3] = 5 + 4 = 9
			*/
			numbers2[i+1] += e
		}
		fmt.Println(maxIndex2, numbers2)
	}

}
func TestIfForSwitch4(t *testing.T) {

	numbers2 := append(make([]int, 0), 1, 2, 3, 4, 5, 6)
	fmt.Println(numbers2)
	maxIndex2 := len(numbers2) - 1

	for i := 0; i <= maxIndex2; i++ {
		e := numbers2[i]
		if i == maxIndex2 {

			numbers2[0] += e
		} else {

			/*
				因为切片是引用类型, 所以, 每次对numbers2的修改都会影响我们的下一次计算, 但是这个其实和range没有关系吧
					numbers2[1] = 2 + 1 = 3
					numbers2[2] = 3 + 2 = 5
					numbers2[3] = 4 + 3 = 7
					numbers2[3] = 5 + 4 = 9
			*/
			numbers2[i+1] += e
		}
		fmt.Println(maxIndex2, numbers2)
	}
}
