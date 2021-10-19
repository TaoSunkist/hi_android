package ch3

import (
	"fmt"
	"unsafe"
)

type Person struct {
	name string
	age  byte
	addr string
}

func main() {
	pp := &Person{"John", 23, "Beijing China"}
	var puptr = uintptr(unsafe.Pointer(pp)) //获取pp的内存地址
	fmt.Println(puptr)
	fmt.Println(&puptr)
	var npp = puptr + unsafe.Offsetof(pp.name)
	fmt.Println(npp)
	var namePtr *string = (*string)(unsafe.Pointer(npp))
	fmt.Println(*namePtr)
	*namePtr = "Lily"
	fmt.Println(pp)
}
