package sort

import (
	"math/rand"
	"testing"
	"sort"
)

//冒泡排序具有稳定性
//时间复杂度分析
//由于需要将每一个元素都和已排序的数组中的每个元素进行比较其实每次操作都是n*n的
var randNumerics sort.IntSlice

func init() {
	for numericsLen := len(randNumerics); numericsLen < 10; numericsLen++ {
		randNumerics = append(randNumerics, rand.Intn(10))
	}
}

func BenchmarkBubbleSort(b *testing.B) {
	cnt := 0
	for i := len(randNumerics) - 1; i >= 0; i-- {
		for j := len(randNumerics) - 1; j >= 0; j-- {
			cnt ++
			if iIndexvValue, jIndexValue := randNumerics[i], randNumerics[j]; iIndexvValue < jIndexValue {
				randNumerics[i] = jIndexValue
				randNumerics[j] = iIndexvValue
			}
		}
	}
	b.Log(cnt, randNumerics)

}

func BenchmarkBubbleSort2(b *testing.B) {
	cnt := 0
	for i := len(randNumerics) - 1; i >= 0; i-- {
		for j := len(randNumerics) - 1; j >= 0; j-- {
			cnt ++
			if randNumerics.Less(i, j) {
				randNumerics.Swap(i, j)
			}
		}
	}
	b.Log(cnt, randNumerics)

}
