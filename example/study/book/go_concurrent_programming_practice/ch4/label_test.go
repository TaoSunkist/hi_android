package ch4

import (
	. "fmt"
	"testing"
)

//TODO Label只能在跳转点的作用域外
func TestFindSpecialCharacter(t *testing.T) {
	var content = "sadSaADDSdsASdfIO19489j大d"
	var evildoer string
	for _, r := range content {
		switch {
		case r >= '\u0041' && r <= '\u005a':
			Println(string(r)) //a-z
		case r >= '\u0061' && r <= '\u007a':
			Println(string(r)) //A-Z
		case r >= '\u4e00' && r <= '\u9fbf':
			Println(string(r)) //A-Z
		default:
			evildoer = string(r)
			goto L2
		}
	}
L2:
	{
		Println("发现阿拉伯数字.", evildoer)
	}
}

/**
 */
func TestFindSpecialCharacter2(t *testing.T) {
	var content = "sadSaADDSdsASdfIO19489j大d"
	var evildoer string
L2:
	for _, r := range content {
		switch {
		case r >= '\u0041' && r <= '\u005a':
			Println(string(r)) //a-z
		case r >= '\u0061' && r <= '\u007a':
			Println(string(r)) //A-Z
		case r >= '\u4e00' && r <= '\u9fbf':
			Println(string(r)) //A-Z
		default:
			evildoer = string(r)
			break L2
		}
	}
	Println("发现阿拉伯数字.", evildoer)
}

/*

//报错invalid break label L2
func findSpecialCharacter3(content string) {
	var evildoer string
L2:
	{
		for _, r := range content {
			switch {
			case r >= '\u0041' && r <= '\u005a':
				Println(string(r)) //a-z
			case r >= '\u0061' && r <= '\u007a':
				Println(string(r)) //A-Z
			case r >= '\u4e00' && r <= '\u9fbf':
				Println(string(r)) //A-Z
			default:
				evildoer = string(r)
				break L2
			}
		}
	}
	Println("发现阿拉伯数字.", evildoer)
}
//死循环
func findSpecialCharacter3(content string) {
	var evildoer string
L2:
	{
	}
	for _, r := range content {
		switch {
		case r >= '\u0041' && r <= '\u005a':
			Println(string(r)) //a-z
		case r >= '\u0061' && r <= '\u007a':
			Println(string(r)) //A-Z
		case r >= '\u4e00' && r <= '\u9fbf':
			Println(string(r)) //A-Z
		default:
			evildoer = string(r)
			goto L2
		}
	}
	Println("发现阿拉伯数字.", evildoer)
}

*/
