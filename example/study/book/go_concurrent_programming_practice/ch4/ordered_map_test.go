package ch4_test

/**
有序的Map
*/
import (
	"log"
	"reflect"
	"sort"
	"testing"
)

type Keys interface {
	//排序接口
	sort.Interface
	//动态类型
	ElementType() reflect.Type
	//添加元素
	add(element interface{}) bool
	//获取元素
	get(index int) interface{}
	getAll() interface{}
	//移除元素
	remove(element interface{})
	//清除所有元素
	clear()
	//查找元素位置
	search(element interface{}) (index int, isExist bool)
	//比较器, 大于=1, 小于=-1, 相等=0
	isTypeConsistency(element interface{}) bool
}

type MyKeys struct {
	elements    []interface{}
	elementType reflect.Type
	compareFunc func(i, j interface{}) int8
}

func (this *MyKeys) Less(i, j int) bool {
	return this.compareFunc(this.elements[i], this.elements[j]) == 1
}

func (this *MyKeys) Swap(i, j int) {
	this.elements[i], this.elements[j] = this.elements[j], this.elements[i]
}

func (this *MyKeys) Len() int {
	return len(this.elements)
}

func (this *MyKeys) ElementType() reflect.Type {
	return this.elementType
}

func (this *MyKeys) add(element interface{}) bool {
	if this.isTypeConsistency(element) {
		defer sort.Sort(this)
		this.elements = append(this.elements, element)
		return true
	} else {
		return false
	}
}

func (this *MyKeys) get(index int) interface{} {
	return this.elements[index]
}

func (this *MyKeys) remove(element interface{}) bool {
	if this.isTypeConsistency(element) == false {
		return false
	}

	if index, isExist := this.search(element); isExist {
		newElements := make([]interface{}, 0)
		for index2, element2 := range this.elements {
			if index2 == index {
				continue
			} else {
				newElements = append(newElements, element2)
			}
		}
		this.elements = newElements

		return true
	} else {
		return false
	}
}

func (this *MyKeys) clear() {
	this.elements = nil
}
func (this *MyKeys) contains(element interface{}) (isExist bool) {
	_, isExist = this.search(element)
	return
}
func (this *MyKeys) search(element interface{}) (index int, isExist bool) { //1和-1就可以确定二分查找的子序列的位置.
	i := sort.Search(this.Len(), func(i int) bool { return this.compareFunc(this.elements[i], element) < 1 })
	if i < this.Len() && this.compareFunc(this.elements[i], element) == 0 {
		return i, true
	} else {
		return 0, false
	}
}
func (this *MyKeys) isTypeConsistency(element interface{}) bool {
	if element == nil || reflect.TypeOf(element).Name() != this.elementType.Name() {
		return false
	} else {
		return true
	}
}
func TestKeys(t *testing.T) {
	myKeys := &MyKeys{
		elements:    make([]interface{}, 0),
		elementType: reflect.TypeOf(int8(1)),
		compareFunc: func(i, j interface{}) int8 {
			if i, j := i.(int8), j.(int8); i > j {
				return 1
			} else if i < j {
				return -1
			} else {
				return 0
			}
		},
	}

	log.Println(myKeys.add(int8(1)))
	log.Println(myKeys.add(int8(6)))
	log.Println(myKeys.add(int8(3)))
	log.Println(myKeys.add(int8(2)))
	log.Println(myKeys.add(int8(7)))
	log.Println(myKeys.add(int8(4)))
	log.Println(myKeys.add(int8(9)))
	log.Println(myKeys)

	myKeys.search(int8(1))
	myKeys.search(int8(2))
	myKeys.search(int8(3))
	myKeys.search(int8(4))
	myKeys.search(int8(5))
	myKeys.search(int8(6))
	myKeys.search(int8(9))
	log.Println()

	// Searching data sorted in descending order would use the <= operator instead of the >= operator.
	log.Println("Removed 9, Result:", myKeys.remove(int8(9)), ", myKeys:", myKeys)
	log.Println("Removed 2, Result:", myKeys.remove(int8(2)), ", myKeys:", myKeys)
}

type OrderedMap struct {
	container MyKeys
	elements  map[interface{}]interface{}
}

//创建集合
func NewOrderedMap() *OrderedMap {
	return &OrderedMap{
		container: MyKeys{
			elements:    make([]interface{}, 0),
			elementType: reflect.TypeOf(int8(1)),
			compareFunc: func(i, j interface{}) int8 {
				return 0
			}},
		elements: make(map[interface{}]interface{})}
}

//由于我们是根据keys中的元素顺序去遍历节点的无序字典结构的，所以sort接口是针对keys的
//比较逻辑

func (this *OrderedMap) Put(key interface{}, value interface{}) {
	this.container.add(key)
	this.elements[key] = value
}
func (this *OrderedMap) Remove(key interface{}) {
	delete(this.elements, key)
}

func TestOrderedMap(t *testing.T) {
	orderedMap := NewOrderedMap()
	orderedMap.Put(1, "a")
	orderedMap.Put(4, "b")
	orderedMap.Put(3, "c")
	orderedMap.Put(2, "d")
	orderedMap.Put(5, "e")
	log.Println(orderedMap)
}
