package ch4

type Set interface {
	Add(element interface{}) bool
	Remove(element interface{})
	Contains(element interface{}) bool
	Clear()
	Len() int
	Same(other Set) bool
	Elements() []interface{}
	String() string
	MkString(delimiter rune) string
}

const (
	HASH_SET = iota
	TREE_SET
)

//超集
func IsSuperset(h, other Set) (IsSuperset bool) {
	if h == nil || other == nil {
		return false
	}
	otherElements := other.Elements()
	for k := range otherElements {
		if h.Contains(k) == false {
			return false
		}
	}
	return true
}

func NewSet() (Set) {
	return New(0)
}

//并集
func Union(h, other Set) (elements Set) {
	if h == nil || other == nil {
		return
	}
	elements = NewSet()
	if hElements := h.Elements(); len(hElements) != 0 {
		for _, element := range hElements {
			elements.Add(element)
		}
	}

	if otherElements := other.Elements(); len(otherElements) != 0 {
		for _, element := range otherElements {
			elements.Add(element)
		}
	}

	return elements
}

//交集
func Intersect(h, other Set) (elements Set) {
	hElements := h.Elements()
	hElementMap := make(map[interface{}]bool, len(hElements))
	for _, value := range hElements {
		hElementMap[value] = true
	}
	otherElements := other.Elements()
	otherElementMap := make(map[interface{}]bool, len(otherElements))
	for _, value := range otherElements {
		otherElementMap[value] = true
	}
	elements = NewSet()
	for k := range hElementMap {
		if otherElementMap[k] {
			elements.Add(k)
		}
	}
	return
}

//差集
func Difference(h, other Set) (elements Set) {
	if h == nil || other == nil {
		return h
	}
	hElements := h.Elements()
	elements = NewSet()
	for index, value := range hElements {
		if other.Contains(hElements[index]) == false {
			elements.Add(value)
		}
	}
	return
}

//对称差集
func SymmetricDifference(h, other Set) (elements Set) {
	hOtherDiffSet := Difference(h, other)
	otherHDiffSet := Difference(other, h)

	return Union(hOtherDiffSet, otherHDiffSet)
}
