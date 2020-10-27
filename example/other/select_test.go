package other

import (
	"fmt"
	"log"
	"math"
	"math/rand"
	"testing"
	"time"
)

var ch chan time.Duration = make(chan time.Duration, 5)

func TestSelect(t *testing.T) {
	log.Println(ch)
	for i := 0; i < 5; i++ {
		a := rand.Int63n(8)
		i1 := i
		go doTask(time.Duration(a), i1)
	}

	for {
		select {
		case a, b := <-ch:
			t.Log(a, b)
		}
	}
}

func doTask(a time.Duration, i1 int) {
	log.Println("goroutine :", a*time.Second)
	time.Sleep(a * time.Second)
	log.Println("goroutine task completed, ", a*time.Second)
	ch <- a
}

func TestLongMax(t *testing.T) {
	t.Log(fmt.Sprintf("%d",math.MaxUint64))

	//ioutil.WriteFile("./1.txt", math.MaxUint64, 777)
}
