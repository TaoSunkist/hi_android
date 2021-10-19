package v1

import (
	"fmt"
	"hi_golang/example/study/book/go_concurrent_programming_practice/ch7/person_handler"
	"log"
	"time"
)

type IPersonHandlerImpl1 struct{}

func (p IPersonHandlerImpl1) Batch(origCh <-chan person_handler.Person) <-chan person_handler.Person {
	newOrigsCh := make(chan person_handler.Person)
	go func() {
		log.Println("ready process person num: ", len(origCh))
		for {
			orig, ok := <-origCh
			if !ok {
				close(newOrigsCh)
				break
			}
			p.Handler(&orig)
			log.Println("processed person: ", orig)
			newOrigsCh <- orig
			time.Sleep(500 * time.Millisecond)
		}
	}()
	return newOrigsCh
}

func (IPersonHandlerImpl1) Save(origCh <-chan person_handler.Person) <-chan byte {
	signCh := make(chan byte)
	go func() {
		for {
			if orig, ok := <-origCh; ok {
				log.Println("save person:", orig, " successfully")
			} else {
				signCh <- 1
				close(signCh)
				break
			}
		}
	}()
	return signCh
}

func (IPersonHandlerImpl1) Handler(p *person_handler.Person) { p.Name += p.Addr }

func NewPersonHandler() person_handler.IPersonHandler { return IPersonHandlerImpl1{} }

func FetchPersons() <-chan person_handler.Person {
	var personCh = make(chan person_handler.Person, 5)
	for loop := 0; loop < 5; loop++ {
		p := person_handler.Person{Name: fmt.Sprintf("name%d", loop), Addr: fmt.Sprintf("addr%d", loop)}
		personCh <- p
		log.Println("produce person: ", p)
	}
	defer close(personCh)
	return personCh
}
