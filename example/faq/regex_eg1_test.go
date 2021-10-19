package faq

import (
	"fmt"
	"strings"
)
import "regexp"
import (
	"testing"
)

var phones = []string{
	"07462811767",
	"17462811767",
	"18684941204",
	"1868494120",
	"27462811767",
	"10000",
	"10al000",
	"10al000a",
	"a1a0al000",
	"10086",
	"10010",
	"13802321232",
	"138023212329",
	"37462811767",
}

func TestSimpleMatchPhone(t *testing.T) {
	reg := regexp.MustCompile(`^1\d{10}$`)
	for _, v := range phones {
		match := reg.MatchString(v)
		fmt.Println(v, match)
	}
}

func TestRegext(t *testing.T) {
	var a = "requires%s diam%sonds/ti%sme，whether to re%scharge%s？"
	var count = strings.Count(a, "%s")
	for i := 0; i < count; i++ {
		a = strings.Replace(a, "%s", fmt.Sprintf("%%%d$s", i+1), 1)
	}
	t.Log(a)
}

func TestRangee(t *testing.T) {
	var users []User
	users = append(users, User{isLiked: 11111})
	users = append(users, User{isLiked: 21111})
	users = append(users, User{isLiked: 31111})
	users = append(users, User{isLiked: 41111})
	users = append(users, User{isLiked: 51111})
	users = append(users, User{isLiked: 61111})

	var users2 []*User
	for _, user := range users {
		users2 = append(users2, &user)
	}
	fmt.Println(users2)

	var users3 []*User
	for i := 0; i < len(users); i++ {
		users3 = append(users3, &users[i])
	}
	fmt.Println(users3)
}

type User struct {
	isLiked uint32
}
