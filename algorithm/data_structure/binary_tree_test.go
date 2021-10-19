package data_structure

import (
	"testing"
)

type Node struct {
	data int
	l    *Node
	r    *Node
}

func TestBinaryTree(t *testing.T) {
	t.Log("Test Binary Tree.")
	n1 := &Node{3, nil, nil}
	n2 := &Node{2, nil, nil}
	n3 := &Node{6, nil, nil}
	n4 := &Node{8, nil, nil}
	n5 := &Node{7, nil, nil}

	n1.l = n2
	n1.r = n5
	n2.l = n3
	n2.r = n4
	//递归遍历
	var foreachTree func(n *Node)
	foreachTree = func(n *Node) {
		t.Log(n)
		if n.l != nil {
			foreachTree(n.l)
		}
		if n.r != nil {
			foreachTree(n.r)
		}
	}
	foreachTree(n1)
}
