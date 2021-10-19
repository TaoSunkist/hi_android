package sort

import "testing"

//简单的两个有序的数组进行合并的算法
var arr1, arr2, mergedArr = []int{1, 3, 4, 6}, []int{2, 5, 7, 9, 10}, make([]int, 0)

func BenchmarkMerge(b *testing.B) {
	i, j, k := 0, 0, 0
	for true {
		if i == len(arr1) || j == len(arr2) {
			if i < len(arr1) {
				mergedArr = append(mergedArr, arr1[i:]...)
			} else if j < len(arr2) {
				mergedArr = append(mergedArr, arr2[j:]...)
			}
			break
		}
		if arr1[i] < arr2[j] {
			mergedArr = append(mergedArr, arr1[i])
			i++
		} else {
			mergedArr = append(mergedArr, arr2[j])
			j++
		}
		k++
		b.Log(k, i, j)
	}
	b.Log(mergedArr)
}
