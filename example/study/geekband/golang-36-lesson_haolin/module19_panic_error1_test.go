package golang36lessonhaolin

import (
	"errors"
	"fmt"
	"testing"
)

/* panic 抛出程序运行错误, recovery catch 他并进行后续处理
 */
func TestPanicError1(t *testing.T) {
	for _, req := range []string{"", "hello!"} {

		fmt.Printf("request: %s\n", req)
		resp, err := echo(req)

		if err != nil {

			fmt.Printf("error: %s\n", err)
			continue
		}

		fmt.Printf("response: %s\n", resp)
	}
}

func echo(request string) (response string, err error) {
	if request == "" {
		err = errors.New("empty request")
		return
	}
	response = fmt.Sprintf("echo: %s", request)
	return
}

func TestPanicError2(t *testing.T) {
	fmt.Println("Enter function main.")
	defer func() {
		fmt.Println("Enter defer function.")
		if p := recover(); p != nil {
			fmt.Printf("panic: %s\n", p)
		}
		fmt.Println("Exit defer function.")
	}() // 引发panic。 panic(errors.New("something wrong")) fmt.Println("Exit function main.")
}
