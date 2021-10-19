package the_way_to_go

import (
	"fmt"
	"testing"
)

/*
创建一个程序，要求能够打印类似下面的结果（直到每行 25 个字符时为止）：

G
GG
GGG
GGGG
GGGGG
GGGGGG
使用 2 层嵌套 for 循环。
使用一层 for 循环以及字符串截断。
*/
func TestCh55q(t *testing.T) {
	a1()
	//a2()
}

func a2() {
	for i1, i, str := 0, 25, "G"; i1 < i; i1++ {
		fmt.Println(str)
		str += "G"
	}
}
func a1() {
	for i := 0; i < 25; i++ {
		fmt.Println("")
		for i1 := 0; i1 < i; i1++ {
			fmt.Print("G")
		}
	}
}
