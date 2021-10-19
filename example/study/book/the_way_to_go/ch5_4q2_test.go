package the_way_to_go

import (
	"fmt"
	"time"
	"testing"
)

func TestCh54q2(t *testing.T) {
	var (
		a = 0
	)
FOR1:
	{
		fmt.Printf("FOR1:%s%2,%v\n", a, a%2)
	FOR2:
		{
			time.Sleep(700 * time.Millisecond)
			a++
			fmt.Println(a)
			if a%2 == 0 {
				goto FOR1
			} else {
				goto FOR2
			}
		}
	}
}
