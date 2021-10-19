package ch8

import (
	"flag"
	"fmt"
	"io"
	"log"
	"net"
	"os"
	"testing"
)

func TestNetcat1(t *testing.T) {
	flag.Parse()
	var port = flag.Arg(0)
	var connCfg = fmt.Sprintf("127.0.0.1:%s", port)
	if conn, err := net.Dial("tcp", connCfg); err != nil {
		log.Fatal(err)
	} else {
		defer conn.Close()
		mustCopy(os.Stdout, conn)
	}
}
func mustCopy(dst io.Writer, src io.Reader) {
	//从port读取，写入到输出流
	if _, err := io.Copy(dst, src); err != nil {
		log.Fatal(err)
	} else {
		log.Println("dail successfully.")
	}
}
