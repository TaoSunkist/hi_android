package leetcode

import (
	"fmt"
	"testing"
)

/* 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
示例:
给定 nums = [2, 7, 11, 15], target = 9
因为 nums[0] + nums[1] = 2 + 7 = 9
所以返回 [0, 1] */
func BenchmarkTwoSum(b *testing.B) {

	var array = []int{3, 2, 4, 5}
	var target = 6
	b.ResetTimer()
	fmt.Printf("%v\n", twoSum(array, target))
}

/* 时间复杂度: O(n^2), 因为每个元素都要被遍历一次, 可以优化的方案有很多,
1. 排序后二分查找
2. 遍历到自己后跳过遍历
执行用时 : 40 ms , 在所有 Go 提交中击败了 34.96% 的用户
内存消耗 : 2.9 MB , 在所有 Go 提交中击败了 100.00% 的用户 */
func twoSum(array []int, target int) []int {

	/* 四数之和也需要这个校验 */
	elementSumFunc := func(array []int) ([]int, int) {

		elementSum := 0
		count := 0

		for _, v := range array {
			count++
			elementSum += v
		}

		if elementSum == target {
			return []int{0, 1}, count
		}
		return []int{}, count
	}

	if indexArray, count := elementSumFunc(array); count <= 2 {
		return indexArray
	} else {

		var arrayLen = len(array)
		for i := 0; i < arrayLen; i++ {
			v := array[i]
			for i1 := i; i1 < arrayLen; i1++ {
				v1 := array[i1]
				if i != i1 && target == v+v1 {
					return []int{i, i1}
				}
			}
		}
		return []int{}
	}
}

/*---------------------------------------------------------------------------------------------------------------------
执行用时 : 4 ms , 在所有 Go 提交中击败了 96.86% 的用户
内存消耗 : 3.4 MB , 在所有 Go 提交中击败了 64.29% 的用户

利用 hashmap, 实现空间换时间的方法 */
func BenchmarkTwoSum2(b *testing.B) {

	var array = []int{3, 2, 4, 5}
	var target = 6
	b.ResetTimer()
	fmt.Printf("%v\n", twoSum2(array, target))
}

func twoSum2(array []int, target int) []int {
	var element2IndexMap = make(map[int]int, len(array))

	for index, value := range array {
		if elementIndex, isExist := element2IndexMap[target-value]; isExist {
			return []int{index, elementIndex}
		}
		element2IndexMap[value] = index
	}
	return []int{}
}

/*---------------------------------------------------------------------------------------------------------------------*/
