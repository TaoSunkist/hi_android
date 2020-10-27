package other

import (
	"fmt"
	"testing"
)

//可以把channel看成是signal，而非一种容器
//signal的三个特性：交付保证、状态、有数据||无数据
func TestNonBufChan1(t *testing.T) {
	fmt.Println("vim-go2")
	ch1 := make(chan string) //non-buffered的chan将在被发送信号前阻塞
	go func() {
		data := <-ch1 //recevie
		fmt.Println(data)
	}()
	ch1 <- "paper" //send
}

func TestNonBufChan2(t *testing.T) {
	fmt.Println("vim-go")
	ch1 := make(chan string, 1) //buffered的chan将不care数据是否被received,从而不被阻塞。
	go func() {
		data := <-ch1 //recevie
		fmt.Println(data)
	}()
	ch1 <- "paper" //send
}

//--------总结：有无buffered的channel，将决定receiving时是否blocking
