package ch5

import (
	"testing"
	"time"
)

// go test -v<冗长模式> -timeout x/TimeUnit<ms,s,m,h> -run <FuncName>
func TestUnitTimeOut(t *testing.T) {
	time.Sleep(5 * time.Second)
	t.Log("func TestUnitTime test complete.")
}
