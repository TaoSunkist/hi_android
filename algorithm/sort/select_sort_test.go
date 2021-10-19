package sort

import "testing"

func BenchmarkSelectSort(b *testing.B) {
	b.Log("SelectSort")
	b.Log(randNumerics)
	for i := 0; i < len(randNumerics); i++ {
		var min = i
		//依次比较0-1，1-2，2-3
		for j := min + 1; j < randNumerics.Len(); j++ {
			if randNumerics.Less(j, min) {
				min = j
			}
		}
		randNumerics.Swap(min, i)
	}
	b.Log(randNumerics)
}
