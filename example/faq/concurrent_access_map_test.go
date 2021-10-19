package faq

import (
	"fmt"
	"runtime"
	"sync"
	"testing"
)

var safeMap = SafeMap{m: map[int]int{
	1: 1, 2: 2, 3: 3, 4: 4, 5: 5, 6: 6, 7: 7, 8: 8,
}}

type SafeMap struct {
	m map[int]int
	s sync.Mutex
}

func (s *SafeMap) Get(key int) int {
	//s.s.Lock()
	var value = s.m[key]
	//s.s.Unlock()
	return value
}
func (s *SafeMap) Set(key int, value int) {
	//s.s.Lock()
	s.m[key] = value
	//s.s.Unlock()
}
func TestConcurrentAccessMap(t *testing.T) {
	runtime.GOMAXPROCS(4)
	var maxVal = 10
	var ch = make(chan int, maxVal)
	for loop := 1; loop <= maxVal; loop += 1 {
		go func() {
			for loop := 1; loop <= 500; loop += 1 {
				safeMap.Set(loop, loop)
				fmt.Println(safeMap.Get(loop))
				if loop == 500 {
					ch <- safeMap.Get(loop)
				}
			}
		}()
	}
	var a, b, c = <-ch, <-ch, <-ch
	fmt.Println(a, b, c)
}
