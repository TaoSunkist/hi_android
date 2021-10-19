package faq

import (
	"fmt"
	"log"
	"runtime"
	"sync"
	"testing"
)

func TestChannel1(t *testing.T) {
	t.Log("TestChannel1")
	var ch1 = make(chan int)
	func() {
		ch1 <- 1
	}()
	select {
	case a := <-ch1:
		log.Println(a)
	}
}

type student struct {
	Name string
	Age  int
}

func TestSlice(t *testing.T) {
	t.Log("TestSlice")
	m := make(map[string]*student)
	stus := []student{
		{Name: "zhou", Age: 24},
		{Name: "li", Age: 23},
		{Name: "wang", Age: 22},
	}
	for _, stu := range stus {
		m[stu.Name] = &stu
		t.Log(stu.Name)
	}
	for k, v := range m {
		t.Log(k, " ", v)
	}
}

func TestGOMAXPROCS(t *testing.T) {
	runtime.GOMAXPROCS(1)
	runtime.NumCPU()
	wg := sync.WaitGroup{}
	wg.Add(20)
	for i := 0; i < 10; i++ {
		go func() {
			fmt.Println("A: ", i)
			wg.Done()
		}()
	}
	for i := 0; i < 10; i++ {
		go func(i int) {
			fmt.Println("B: ", i)
			wg.Done()
		}(i)
	}
	wg.Wait()
}

const CONST1 = iota

func TestSelectChan(t *testing.T) {
	var i = 0
	t.Log(&i)
	runtime.GOMAXPROCS(1)
	intChan := make(chan int, 1)
	stringChan := make(chan string, 1)
	intChan <- 1
	stringChan <- "hello"
	select {
	case value := <-intChan:
		fmt.Println(value)
	case value := <-stringChan:
		panic(value)
	}
}
func say(s string) {
	for i := 0; i < 2; i++ {
		runtime.Gosched()
		fmt.Println(s)
	}
}

//0,hello
func TestGoSched(t *testing.T) {
	go say("world")
	say("hello")
}

/*---------*/
type threadSafeSet struct {
	sync.RWMutex
	s []interface{}
}

func (set *threadSafeSet) Iter() <-chan interface{} {
	//ch := make(chan interface{}) // 解除注释看看！
	ch := make(chan interface{}, len(set.s)-1)
	go func() {
		//set.RLock()

		for elem, value := range set.s {
			ch <- elem
			println("Iter:", elem, value)
		}

		close(ch)
		//set.RUnlock()

	}()
	return ch
}

func TestThreadSafeSet(t *testing.T) {
	th := threadSafeSet{
		s: []interface{}{"1", "2"},
	}
	v := <-th.Iter()
	t.Logf("%s%v", "ch", v)
}

func TestChannel2(t *testing.T) {
	ch := make(chan int, 1)
	<-ch
}
