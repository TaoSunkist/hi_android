package golang36lessonhaolin

import (
	"fmt"
	"testing"
)

func TestChannel(t *testing.T) {
	/*
		channel的基本特性
		1.对于同一个通道，发送操作之间是互斥的，接收操作之间也是互斥的。
			直到这个元素值被完全复制进该通道之后，其他针对该通道的发送操作才可能被执行

		2.发送操作和接收操作中对元素值的处理都是不可分割的。
			比如发送操作一定会和元素的复制操作被完成后才会进行下一步动作, 而一个接受操作通过包含了三个步骤
			copy element for on channel/received copy-element for on channel/delete origin element for on channel
		3.发送操作在完全完成之前会被阻塞。接收操作也是如此。

		先说针对缓冲通道的情况。如果通道已满，那么对它的所有发送操作都会被阻塞，直到通道中有元素值被接收走。这时，通道会优先通知最早因此而等
		待的、 那个发送操作所在的 goroutine，后者会再次执行发送操作。由于发送操作在这种情况下被阻塞后，它们所在的 goroutine 会顺序地进入通
		道内部 的发送等待队列，所以通知的顺序总是公平的。
	*/
	ch1 := make(chan int, 3)
	ch1 <- 2
	ch1 <- 1
	ch1 <- 3
	elem1 := <-ch1
	fmt.Printf("The first element received from channel ch1: %v\n", elem1)
}

/*
这涉及了它们什么时候会互斥，什么时候会造成阻塞，什么时候会引起 panic，以及它们收发元素值的顺序是怎样的，它们是怎样保证元素值的完整性的，
元素值通常会被复制几次，等等*/

/*
通道底层存储数据的是链表还是数组？
作者回复: 环形链表

不要从接受端关闭channel算是基本原则了，另外如果有多个并发发送者，1个或多个接收者，有什么普适选择可以分享吗？
作者回复: 可以用另外的标志位做，比如context。
*/

func TestChannel2(t *testing.T) {
	ch := make(chan string, 3) // 创建缓冲区为3 的 通道
	ch <- "a"                  // 标注1 main goroutine 向 通道ch 发送 "a"
	ch <- "b"                  // 标注2 main goroutine 向 通道ch 发送 "b"
	ch <- "c"                  // 标注3 main goroutine 向 通道ch 发送 "c"
	<-ch                       // 标注4 请问，接收 通道ch 数据 的是哪个goroutine
	ch <- "d"                  // 标注5 main goroutine 向 通道ch 发送 "d"
}
