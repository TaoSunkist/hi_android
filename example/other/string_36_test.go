package other

import (
	"testing"
	"log"
	"fmt"
)

var AsciiMaps map[string]int

func init() {
	log.Println(rune('0'))
	log.Println(rune('z'))
	for loop := rune('0'); loop <= rune('z'); loop++ {
		log.Println(loop)
	}
	fmt.Sprint("----", rune('a'))
	//36
}

func TestString36(t *testing.T) {
	t.Log("TestString36")
	str := "1b"
	t.Log(rune(str[0]))
	//0-9 a-z
	//1b = 1 * 36^1 + 11 * 36^0 = 48
	for i := len(str) - 1; i >= 0; i-- {
		iRune := rune(str[i])
		t.Log(iRune)
	}
	t.Log("2^4=", Cifang(2, 12)) //16
}

func Cifang(baseNum int64, power int64) int64 {
	if power > 1 {
		power--
		baseNum = baseNum * Cifang(baseNum, power)
	}
	return baseNum
}
