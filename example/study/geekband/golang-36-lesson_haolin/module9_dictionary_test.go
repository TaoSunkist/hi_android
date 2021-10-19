package golang36lessonhaolin

import (
	"fmt"
	"testing"
)

/*
https://time.geekbang.org/column/article/14123
map是一种根据hash值存储k-v形式数据的一种容器, 由hash function 以及 hash bucket 实现 哈希表会先用这个键哈希值的低几位去定位到一个
哈希桶，然后再去这个哈希桶中，查找这个键.在 Go 语言的字典中，每一个键值都是由它的哈希值代表的。也就是说，字典不会独立存储任何键的值，但会
独立存储它们的哈希值
什么是hash
常用的hash算法
hash算法的四个特性
hash和md5的区别

Go 语言字典的键类型不可以是函数类型、字典类型和切片类型。

map的key类型最好不要使用interface类型, unless代码是可控的, otherwise会引发panic, 因为map的key的类型是有约束的
*/
func TestDictionaryOperation(t *testing.T) {
	fmt.Println("TestDictionaryOperation")
	/* 你可能会有疑问，为什么键类型的值必须支持判等操作？我在前面说过，Go 语言一旦定位到了某一个哈希桶，那么就会试图在这个桶中查找键值。具体是怎么找的呢？
	首先，每个哈希桶都会把自己包含的所有键的哈希值存起来。Go 语言会用被查找键的哈希值与这些哈希值逐个对比，看看是否有相等的。如果一个相等的都没有，那么就
	说明这个桶中没有要查找的键值，这时 Go 语言就会立刻返回结果了。如果有相等的，那就再用键值本身去对比一次。为什么还要对比？原因是，不同值的哈希值是可能
	相同的。这有个术语，叫做“哈希碰撞”。所以，即使哈希值一样，键值也不一定一样。如果键类型的值之间无法判断相等，那么此时这个映射的过程就没办法继续下去了。
	最后，只有键的哈希值和键值都相等，才能说明查找到了匹配的键 - 元素对。*/
	/*var func1 = func() {}
	var func2 = func() {}
	fmt.Println(func1 == func2)
	var slice1 =make([]int,5)
	var slice2 =make([]int,5)
	fmt.Println(slice1 == slice2)
	var map1 = make(map[string]string)
	var map2 = make(map[string]string)
	fmt.Println(map1==map2)*/
	/*var a = nil == nil
	fmt.Println(a)
	var map3 = map[interface{}]bool{
		nil: true,
	}
	fmt.Println(map3)*/
}

/*判断一个操作是否是原子的可以使用 go run race 命令做数据的竞争检测*/
func TestGoroutine(t *testing.T) {
	var targetElement = 0
	go func() {
		for ; true; {
			targetElement++
			fmt.Printf("go1 targetElement: %d\n", targetElement)
		}
	}()
	go func() {
		for ; true; {
			targetElement++
			fmt.Printf("go2 targetElement: %d\n", targetElement)
		}
	}()
	select {}
}
