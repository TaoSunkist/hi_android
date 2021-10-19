package ch8

import (
	"net"
	"log"
	"testing"
)

func TestFtpsrv(t *testing.T) {
	if ln, err := net.Listen("tcp", "127.0.0.1:9010"); err != nil {
		log.Fatal("cause by:", err)
	} else {
		log.Println("tcp listen to 127.0.0.1:9010 successfully.")
		defer ln.Close()
		for {
			if conn, err := ln.Accept(); err != nil {
				log.Println("cause by:", err)
			} else {
				defer conn.Close()
				log.Println(conn.LocalAddr(), "<-", conn.RemoteAddr(), ".")
				var bts []byte
				if n, err := conn.Read(bts); err != nil {
					log.Println("read body size:", n)
				}
			}
		}
	}
}
