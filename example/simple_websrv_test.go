package example

import (
	"bytes"
	"log"
	"net"
	"testing"
	"io"
	"strings"
)

func TestTcp1(t *testing.T) {

	connFunc := func(conn net.Conn) {
		stringBuilder := strings.Builder{}
		buffer := bytes.NewBuffer(make([]byte, 5))
		for {
			if _, err := conn.Read(buffer.Bytes()); err != nil {
				log.Println(err)
				if err == io.EOF {
					log.Println("client closed connection.")
					break
				}
			} else {
				stringBuilder.WriteString(buffer.String())
			}
		}
		log.Println(stringBuilder.String())
		t.Log(stringBuilder.String())
	}

	if tcpListener, err := net.ListenTCP("tcp", &net.TCPAddr{IP: net.ParseIP("127.0.0.1"), Port: 8000, Zone: ""}); err != nil {
		t.Error(err)
	} else {
		defer tcpListener.Close()
		for {
			if conn, err := tcpListener.Accept(); err != nil {
				log.Fatal(err)
			} else {
				go connFunc(conn)
			}
		}
	}
}
