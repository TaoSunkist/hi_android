package golang36lessonhaolin

import (
	"errors"
	"fmt"
	"testing"
)

type Printer func(contents string) (n int, err error)

func printToStd(contents string) (bytesNum int, err error) { return fmt.Println(contents) }

func TestModule12Function(t *testing.T) {
	t.Log("TestModule12Function")
	var p Printer
	p = printToStd
	p("something")
}

type operate func(x, y int) int

func calculate(x int, y int, op operate) (int, error) {
	if op == nil {
		return 0, errors.New("invalid operation")
	}
	return op(x, y), nil
}

func TestModule12Function1(t *testing.T) {

	array1 := [3]string{"a", "b", "c"}
	fmt.Printf("The array: %v\n", array1)
	array2 := modifyArray(array1)
	fmt.Printf("The modified array: %v\n", array2)
	fmt.Printf("The original array: %v\n", array1)
}

func modifyArray(a [3]string) [3]string {
	a[1] = "x"
	return a
}

/*深白色说的很对！作为切片的话，将会影响原数组，毕竟我们知道切片的数据是通过指向地址取值，而函数进行对原数组修改，只是先拷贝一份，然后再修改，根本修改不到原数组。
函数返回指针类型，的确不会发生拷贝，但是也是将指针值拷贝了，再返回，所以其实深究，还是有拷贝在里面的，若是返回非指针类型的结果，一定会发生拷贝。
go没有引用传递，只有值传递，所以基本上都是值拷贝。*/
/*注意，对于引用类型，比如：切片、字典、通道，像上面那样复制它们的值，只会拷贝它们本身而已，并不会拷贝它们引用的底层数据。也就是说，这时只是浅表复制，而不是深层复制。*/
/* deep copier library: https://github.com/jinzhu/copier*/
