package ch4

import (
	"log"
	"testing"
)

func TestDefer(t *testing.T) {
	log.Println(DeferB())

}
func DeferB() (int) {
	var number = 1
	defer DeferA(func(number *int) (*int) {
		return number
	}(&number))
	log.Println(number)
	number++
	log.Println(number)
	return number
}
func DeferA(number *int) {
	*number = *number + 1
}
