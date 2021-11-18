package main

import (
	"fmt"
	"regexp"
	"strconv"
	"strings"
	"testing"
)

func TestCh55q2(t *testing.T) {
	sampleRegexp := regexp.MustCompile(`/^[0-9]+.?[0-9]*$/`)

	fmt.Println("For regex \\d")
	match := sampleRegexp.MatchString("19009")
	fmt.Printf("For 1: %t\n", match)

	fmt.Println("taohui")
}
