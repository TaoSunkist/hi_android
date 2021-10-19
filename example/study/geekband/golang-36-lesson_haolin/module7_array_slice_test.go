package golang36lessonhaolin

import (
	"fmt"
	"math/rand"
	"testing"
)

func TestSliceCap(t *testing.T) {
	// 示例1。
	s1 := make([]int, 5)
	fmt.Printf("The length of s1: %d\n", len(s1))
	fmt.Printf("The capacity of s1: %d\n", cap(s1))
	fmt.Printf("The value of s1: %d\n", s1)
	s2 := make([]int, 5, 8)
	fmt.Printf("The length of s2: %d\n", len(s2))
	fmt.Printf("The capacity of s2: %d\n", cap(s2))
	fmt.Printf("The value of s2: %d\n", s2)

	s3 := []int{1, 2, 3, 4, 5, 6, 7, 8}
	s4 := s3[3:6] //[3,6)
	fmt.Printf("The length of s4: %d\n", len(s4))
	fmt.Printf("The capacity of s4: %d\n", cap(s4))
	fmt.Printf("The value of s4: %d\n", s4)
	fmt.Printf("s4[0:cap(s4)]: %d\n", s4[0:cap(s4)])
}

/*
1.当两个长度不一的切片使用同一个底层数组, 并且两切片的长度均小于数组的容量时, 对其中长度较小的一个切片进行append操作,
  但不超过底层数组容量, 这时会影响长度较长切片中原来比较小切片多看到的值, 因为底层数组被修改了。
2.可以截取切片的部分数据, 然后创建新数组来缩容
*/
func TestSliceCap2(t *testing.T) {
	fmt.Println("TestSliceCap2------------------------")
	defer fmt.Println()
	s1 := make([]int, 5, 10)
	for i := range s1 {
		s1[i] = rand.Int()
		fmt.Printf("\ns1 index: %d, value, %d", i, s1[i])
	}

	s2 := s1[1:3] /* [1,3) 1, 2 */
	s2 = append(s2, 3)
	for i := range s2 {
		fmt.Printf("\ns2: %d", s2[i])
	}

	for i := range s1 {
		s1[i] = rand.Int()
		fmt.Printf("\ns1 index: %d, value, %d", i, s1[i])
	}
}

/*
新 slice 预留的 buffer 大小是有一定规律的。网上大多数的文章都是这样描述的：
当原 slice 容量小于 1024 的时候, 新 slice 容量变成原来的 2 倍；原 slice 容量超过 1024, 新 slice 容量变成原来的1.25倍。
我在这里先说结论：以上描述是错误的。
当向 slice 中添加元素 1280 的时候, 老 slice 的容量为 1280, 之后变成了 1696, 两者并不是 1.25 倍的关系（1696/1280=1.325）。添加完 1696 后, 新的容量 2304 当然也不是 1696 的 1.25 倍。
*/
func TestSliceCapital(t *testing.T) {
	fmt.Println("TestSliceCapital")
	s := make([]int, 0)

	oldCap := cap(s)

	for i := 0; i < 2048; i++ {
		s = append(s, i)
		newCap := cap(s)
		if newCap != oldCap {
			fmt.Printf("[%d -> %4d] cap = %-4d  |  after append %-4d  cap = %-4d\n", 0, i-1, oldCap, i, newCap)
			oldCap = newCap
		}
	}
	/* 内存对齐的操作会影响slice的cap增长
	内存对齐的说明:
	尽管内存是以字节为单位, 但是大部分处理器并不是按字节块来存取内存的.
	它一般会以双字节,四字节,8字节,16字节甚至32字节为单位来存取内存, 我们将上述这些存取单位称为内存存取粒度.
	*/

}
