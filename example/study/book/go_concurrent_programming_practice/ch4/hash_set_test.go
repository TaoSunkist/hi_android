package ch4

import (
	"bytes"
	"fmt"
	"log"
	"strings"
	"testing"
)

func TestHashSet(t *testing.T) {
	var hashSet = New(0)
	var funcs = []func(a, b int){
		func(a, b int) {
			log.Println("a--->", a, b)
		},
		func(a, b int) {
			log.Println("a--->", a, b)
		},
	}

	log.Println(funcs)
	log.Println(hashSet.Add("a"))
	log.Println(hashSet.Add("d"))
	log.Println(hashSet.Add("a"))
	log.Println(hashSet.Add("e"))
	log.Println(hashSet.Add("f"))
	log.Println(hashSet)
	log.Println("Elements:", hashSet.Elements())
	log.Println(hashSet)
	hashSet.Remove("c")
	hashSet.Remove("a")
	log.Println(hashSet)
	log.Println(hashSet.MkString('.'))
	hashSet.Clear()
	log.Println(hashSet)
	log.Println(hashSet.Same(hashSet))

	hashSet.Add(1)
	hashSet.Add(2)
	hashSet.Add(3)
	hashSet.Add(4)
	hashSet.Add(5)

	var hashSet2 = NewSet()
	hashSet2.Add(1)
	hashSet2.Add(3)
	hashSet2.Add(5)
	hashSet2.Add(7)
	hashSet2.Add(9)

	log.Println(hashSet, hashSet2)

	//log.Println("Same:", IsSuperset(hashSet, hashSet2))
	log.Println("Union:", Union(hashSet, hashSet2))
	log.Println("Difference:", Difference(hashSet, hashSet2))
	log.Println("Intersect:", Intersect(hashSet, hashSet2))
	log.Println("SymmetricDifference:", SymmetricDifference(hashSet, hashSet2))
	log.Println(hashSet, hashSet2)
}

type HashSet struct {
	elements map[interface{}]bool
}

func New(len int) *HashSet {
	return &HashSet{elements: make(map[interface{}]bool, len)}
}

func (h *HashSet) Add(element interface{}) (result bool) {
	if !h.elements[element] {
		h.elements[element], result = true, true
	}
	return result
}

func (h *HashSet) Remove(element interface{}) {
	delete(h.elements, element)
}

func (h *HashSet) Contains(element interface{}) (bool) {
	return h.elements[element]
}
func (h *HashSet) Clear() {
	h.elements = make(map[interface{}]bool)
}

func (h *HashSet) Len() (int) {
	return len(h.elements)
}

func (h *HashSet) Same(other Set) (result bool) {
	if other == nil {
		return
	} else if strings.Compare(h.String(), other.String()) == 0 {
		return
	} else {
		return true
	}
}

func (h *HashSet) Elements() ([]interface{}) {
	log.Println("Elements")
	initializeLen := h.Len()
	elements := make([]interface{}, initializeLen)
	actuallyLen := 0
	for k := range h.elements { //遍历所有元素
		if actuallyLen < initializeLen { //如果实际长度小于初始化长度，那么直接赋值即可
			elements[actuallyLen] = k
		} else { //如果实际长度大于初始化长度，做append追加元素操作
			elements[actuallyLen] = append(elements, k)
		}
		actuallyLen++
	}
	if actuallyLen < initializeLen {
		elements = elements[:actuallyLen]
	}
	return elements
}

func (h *HashSet) MkString(delimiter rune) string {
	var bytesBuf = bytes.Buffer{}
	bytesBuf.WriteString("HashSet{")

	for k := range h.elements { //判断类型写入到bytes中
		bytesBuf.WriteString(fmt.Sprintf("%v, ", k))
	}
	bytesBuf.WriteString("}")
	return bytesBuf.String()
}

func (h *HashSet) String() string {
	var bytesBuf = bytes.Buffer{}
	for k := range h.elements { //判断类型写入到bytes中
		switch k := k.(type) {
		case string:
			bytesBuf.Write([]byte(k))
			break
		case uint, uint8, uint16, uint32, uint64, int, int8, int16, int32, int64:
			bytesBuf.Write([]byte(fmt.Sprintf("%d", k)))
			break
		}
	}
	return bytesBuf.String()
}
