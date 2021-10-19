package ch8

import (
	"fmt"
	"io"
	"log"
	"net"
	"testing"
	"time"
)

func TestClock1(t *testing.T) {
	if ln, err := net.Listen("tcp", "127.0.0.1:9005"); err != nil {
		log.Fatal(err)
	} else {
		log.Println("connection successfully.")
		defer ln.Close()
		for {
			//只能处理一个连接
			if conn, err := ln.Accept(); err != nil { //accept是阻塞的
				log.Print(err)
				continue
			} else {
				handleMsg(conn)
			}
		}
	}
}

func handleMsg(conn net.Conn) {
	defer conn.Close()
	for {
		if _, err := io.WriteString(conn, fmt.Sprintf("%s\n", time.Now().Format("2006-01-02 15:04:05;"))); err != nil {
			return
		}
		time.Sleep(1 * time.Second)
	}
}

//时区池
