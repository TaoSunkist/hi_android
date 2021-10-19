package golang36lessonhaolin

import (
	"container/list"
	"container/ring"
	"fmt"
	"testing"
)

type Junior struct {
	list.Element
	name string
}

func TestList(t *testing.T) {
	var l1 = list.New()

	l1.PushBack(Junior{name: "a"})
	l1.PushFront(Junior{name: "b"})
	l1.PushBack(Junior{name: "c"})

	for l1f1 := l1.Front(); l1f1.Value != nil; {
		fmt.Print(l1f1)
		l1f1 = l1f1.Next()
	}
	fmt.Println()
	//MoveToFront方法和MoveToBack方法, 分别用于把给定的元素移动到链表的最前端和最后端。
	//l1.MoveToFront() l1.MoveToBack()

	//MoveBefore方法和MoveAfter方法, 它们分别用于把给定的元素移动到另一个元素的前面和后面,
	//l1.MoveAfter() l1.MoveBefore()

	//Front和Back方法分别用于获取链表中最前端和最后端的元素,
	//l1.Front() l1.Back() l1.Remove()

	//InsertBefore和InsertAfter方法分别用于在指定的元素之前和之后插入新元素，
	//l1.InsertBefore() l1.InsertAfter()

	//PushFront和PushBack方法则分别用于在链表的最前端和最后端插入新元素。
	//l1.PushFront() l1.PushBack()

	//var l list.List声明的变量l的值,为什么可以直接用来使用
	//延迟初始化的缺点

}

func TestRing(t *testing.T) {
	//Ring值的Len方法的算法复杂度是 O(N) 的，而List值的Len方法的算法复杂度则是 O(1) 的。这是两者在性能方面最显而易见的差别。
	var r ring.Ring //语句声明的r将会是一个长度为1的循环链表
	fmt.Println(r)
}
