package search

import (
	"log"
	"math/rand"
	"sort"
	"testing"
)

type IntArr []int

var arr IntArr

func init() {
	for i := 0; i < 30; i++ {
		arr = append(arr, rand.Intn(100))
	}
	log.Println(arr)
}
func (this IntArr) Less(i, j int) bool {
	return this[i] > this[j]
}
func (this IntArr) Len() int {
	return len(this)
}
func (this IntArr) Swap(i, j int) {
	this[i], this[j] = this[j], this[i]
}
func TestBinarySearch(t *testing.T) {
	sort.Sort(arr)

	arrLen := len(arr)
	target := 0
	t.Log(arr)
	high := arrLen - 1
	low := 0
	for low <= high {
		mid := (low + high) / 2
		if arr[mid] == target {
			t.Log(arr[mid], mid, target)
			break
		}
		if target > arr[mid] {
			high = mid - 1
		} else {
			low = mid + 1
		}
	}
}
