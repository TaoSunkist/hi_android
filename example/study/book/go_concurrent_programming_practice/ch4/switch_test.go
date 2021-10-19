package ch4

import (
	"fmt"
	"log"
	"math/rand"
	"testing"
)

type MyByte byte

func (m *MyByte) printValue() {
	fmt.Println("address:", &m, ", value:", *m, ".")
}
func TestTypeSwitch(t *testing.T) {
	var a interface{} = MyByte(2)

	switch a1 := a.(type) {
	default:
		log.Println("a is unknown type.")
	case MyByte:
		a1.printValue()
		//TODO fallthrough, 类型switch中不允许存在判定下坠
	case int, int8, int16, uint8, uint16:
		log.Println("a:", a1, " is integer,", fmt.Sprintf(" bit: %b .", a))
	}
}

func TestExpressionSwitch(t *testing.T) {
	var str = "中国"
	log.Println(str, str[0], byte(str[0]))
	//var x int = 'b'
	//log.Println(x)
	var characters = []interface{}{"a", 1, 1.0, 1.0000000002, 0x12, 'b', nil}
	log.Println(characters)
	for k := range characters {
		switch character := characters[k]; character {
		default:
			log.Println(character, "unknownn value")
			break
		case "a", "A":
			log.Println("value:", character, "type is string.")
		case 'b':
			log.Println("value:", character, "type is int.")
		case 0x11111:
			log.Println("value:", character, "type is integer.")
		case nil:
			log.Println("value:", character, "untype.")
		}
	}
}
func TestExpressionSwitch2(t *testing.T) {
	var x = 2
	switch a := x * 2; a {
	case 4:
		log.Println("4.")
		fallthrough //TODO 忽视条件
	case 5:
		log.Println("5.")
	}
	var number = rand.Int31n(100)
	switch /*number := 19;*/ { //TODO 作为串联的if逻辑判断
	case number > 50:
		log.Printf("number: %d > 50, result: %t \n", number, number > 50)
	case number < 20:
		log.Printf("number: %d < 20, result: %t \n", number, number < 20)
	}
}
