package v2

import (
	"fmt"
	"hi_golang/example/study/book/go_concurrent_programming_practice/ch7/person_handler"
	"log"
	"sync/atomic"
	"time"
)

type IPersonHandlerImpl struct{}

func FetchPersons(personCh chan<- person_handler.Person) {
	go func() {
		for {
			tag := atomic.AddInt64(&person_handler.A, 1)
			p := person_handler.Person{Name: fmt.Sprintf("name%d", tag), Addr: fmt.Sprintf("addr%d", tag)}
			log.Println("[FetchPerson2G1] a:", tag, " p:", p)
			personCh <- p
			time.Sleep(500 * time.Millisecond)
		}
	}()
	go func() {
		for {
			tag := atomic.AddInt64(&person_handler.A, 1)
			p := person_handler.Person{Name: fmt.Sprintf("name%d", tag), Addr: fmt.Sprintf("addr%d", tag)}
			log.Println("[FetchPerson2G2] a:", tag, " p:", p)
			personCh <- p
			time.Sleep(500 * time.Millisecond)
		}
	}()
}

var newOrigsCh = make(chan person_handler.Person)

func (p IPersonHandlerImpl) Batch(origCh <-chan person_handler.Person) (<-chan person_handler.Person) {
	go func() {
		log.Println("ready process person num: ", len(origCh))
		for {
			orig, _ := <-origCh
			p.Handler(&orig)
			log.Println("processed person: ", orig)
			newOrigsCh <- orig
			time.Sleep(200 * time.Millisecond)
		}
	}()
	return newOrigsCh
}

var signCh = make(chan byte)

func (IPersonHandlerImpl) Save(origCh <-chan person_handler.Person) (<-chan byte) {

	go func() {
		for {
			if orig, ok := <-origCh; ok {
				log.Println("save person:", orig, " successfully")
				signCh <- 1
			} else {
				break
			}
		}
	}()
	return signCh
}

func (IPersonHandlerImpl) Handler(p *person_handler.Person) { p.Name += p.Addr }

func NewPersonHandler() person_handler.IPersonHandler { return IPersonHandlerImpl{} }
