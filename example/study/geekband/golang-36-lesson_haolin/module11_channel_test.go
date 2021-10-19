package golang36lessonhaolin

import (
	"fmt"
	"math/rand"
	"testing"
	"time"
)

type INotifier interface {
	sendInt(ch chan<- int32)
}
type Notifier struct {
	INotifier
}

func Test11Channel(t *testing.T) {
	var ch = make(chan int32)
	var n = Notifier{}
	/*that channel auto the convert to oneway-channel*/
	n.sendInt(ch)
}

func Test11Select(t *testing.T) {
	var chans = []chan int{
		make(chan int, 1),
		make(chan int, 1),
		make(chan int, 1),
		make(chan int, 1),
		make(chan int, 1),
	}
	go func() {

		var chansLen = len(chans)
		for {
			rand.Seed(time.Now().Unix())
			var currentChanIndex = rand.Intn(chansLen)
			var currentChan = chans[currentChanIndex]
			currentChan <- currentChanIndex
			if currentChanIndex == 1 {
				close(currentChan)
			}
			t.Log("send message to chan from chans, current chan of chans the index: ", currentChanIndex)
			time.Sleep(time.Second * 3)
		}
	}()
	var processFromChansMessage = func(a int, isOkay bool) {
		t.Logf("recived a message from chans the currentChanIndex: %d, chan is available: %t.", a, isOkay)
	}
	for {
		select {
		case a, isOkay := <-chans[0]:
			{
				processFromChansMessage(a, isOkay)
			}
		case a, isOkay := <-chans[1]:
			{
				processFromChansMessage(a, isOkay)
			}
		case a, isOkay := <-chans[2]:
			{
				processFromChansMessage(a, isOkay)
			}
		case a, isOkay := <-chans[3]:
			{
				if isOkay {
					processFromChansMessage(a, isOkay)
				} else {
					goto skipChan3
				}
			}
		case a, isOkay := <-chans[4]:
			{
				processFromChansMessage(a, isOkay)
			}
		}
	}
skipChan3:
	{
		t.Log("process other.")
	}
}

func Test11Select1(t *testing.T) {
	intChan := make(chan int, 1)
	// 一秒后关闭通道。
	time.AfterFunc(time.Second, func() {
		close(intChan)
	})
	for {
		select {
		case _, ok := <-intChan:
			if !ok {
				fmt.Println("The candidate case is closed.")
				break
			}
			fmt.Println("The candidate case is selected, is closed:", ok, ".")
		}
	}
}

func Test11Select2(t *testing.T) {
	intChan2 := make(chan int, 5)
	intChan2 <- 0
	_, ok := <-intChan2
	fmt.Println(ok)
	close(intChan2)
	_, ok = <-intChan2
	fmt.Println(ok)
}
