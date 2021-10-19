package the_way_to_go

import (
	"fmt"
	"testing"
)

func TestCh55q2(t *testing.T) {
	test1()
}

func test1() {
	for i := 0; i < 25; i++ {
		fmt.Println("")
		for i1 := 0; i1 < i; i1++ {
			fmt.Print("G")
		}
	}
}
