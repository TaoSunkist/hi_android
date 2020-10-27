package other

import (
	"testing"
	"reflect"
	"log"
)

type Animal interface {
	Speak() string
}
type (
	Cat struct{}
	Dog struct{}
)

func (d *Dog) Speak() string {
	log.Println(reflect.TypeOf(d).Kind().String())
	return "Woof."
}
func (c Cat) Speak() string {
	log.Println(reflect.TypeOf(&c).Kind().String())
	return "Meow."
}

func TestInterface(t *testing.T) {
	animals := []Animal{&Dog{}, Cat{}}
	for _, v := range animals {
		t.Log(v, reflect.TypeOf(v), "Say:", v.Speak())
	}
	cat := new(Cat)
	cat1 := Cat{}
	t.Log(reflect.TypeOf(cat).Kind(), reflect.TypeOf(cat1).Kind())
}

var map1 = map[string]interface{}{
	"cat": Cat{},
	"dog": Dog{},
}

func TestReflectNewStruct(t *testing.T) {
	type_ := reflect.ValueOf(map1["cat"]).Type()
	cat := reflect.New(type_).Interface()
	t.Log(reflect.TypeOf(cat), cat.(*Cat).Speak())
}
