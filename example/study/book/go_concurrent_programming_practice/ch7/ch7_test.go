package ch7

import (
	"hi_golang/example/study/book/go_concurrent_programming_practice/ch7/person_handler"
	"hi_golang/example/study/book/go_concurrent_programming_practice/ch7/person_handler/v1"
	"hi_golang/example/study/book/go_concurrent_programming_practice/ch7/person_handler/v2"
	"log"
	"testing"
	"time"
)

//Person的处理2----------------------------------------------------------------------------------------------------------

func TestPersonBatchProcessV2(t *testing.T) {
	personCh := make(chan person_handler.Person)
	personHandler := v2.NewPersonHandler()
	newPersonCh := personHandler.Batch(personCh)
	v2.FetchPersons(personCh)
	signCh := personHandler.Save(newPersonCh)
	for {

		if sign, ok := <-signCh; ok {
			log.Println(sign, ": completed.")
		} else {
			break
		}
	}
}

//Person的处理1----------------------------------------------------------------------------------------------------------

func TestPersonBatchProcessV1(t *testing.T) {
	personHandlerImpl := v1.NewPersonHandler()
	personCh := v1.FetchPersons()
	newPersonCh := personHandlerImpl.Batch(personCh)
	sign := personHandlerImpl.Save(newPersonCh)
	<-sign
}

//Goroutine和Channel的简单使用--------------------------------------------------------------------------------------------

func BenchmarkChan1(t *testing.B) {
	ch := make(chan int, 5)
	sign := make(chan byte, 2)
	go func() {
		for i := 0; i < 5; i++ {
			ch <- i
			t.Log("The channel cap: ", cap(ch), ", len: ", len(ch))
			time.Sleep(500 * time.Millisecond)
		}
		close(ch)
		t.Log("The channel is closed")
		sign <- 0
	}()
	go func() {
		for {
			e, ok := <-ch
			t.Log(e, ok)
			if !ok {
				break
			}
		}
		time.Sleep(1200 * time.Millisecond)
		sign <- 1
	}()
	t.Log(<-sign)
	t.Log(<-sign)

}
