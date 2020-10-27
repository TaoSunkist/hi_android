package other

import (
	"testing"
	"math/big"
)

func TestNumeric1(t *testing.T) {
	t.Log("TestNumeric1")
	numericStr := "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
	numeric2Str := "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
	numeric := big.Int{}
	numeric2 := big.Int{}
	numericInt, _ := numeric.SetString(numericStr, 10)
	numeric2Int, _ := numeric2.SetString(numeric2Str, 10)
	t.Log(numeric.Div(numericInt, numeric2Int))

}
func TestForeach(t *testing.T){
}