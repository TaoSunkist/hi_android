package ch8

import (
	"fmt"
	"io"
	"log"
	"net"
	"testing"
	"time"
)

var srvs = map[string]struct {
	id       int
	timezone string
	connCfg  string
	conn     net.Conn
}{"BrazilEastSrv": {id: 1, timezone: "Brazil/East", connCfg: "127.0.0.1:9007"},
	"AustraliaDarwinSrv": {id: 2, timezone: "Australia/Darwin", connCfg: "127.0.0.1:9008"}}

var ch1 = make(chan int, 2)

func TestClockwall(t *testing.T) {
	for k, srv := range srvs {
		go func() {
			if ln, err := net.Listen("tcp", srv.connCfg); err != nil {
				log.Fatal(fmt.Sprintf("%s : %s conn failed,", k, srv.connCfg))
			} else {
				log.Println(fmt.Sprintf("%s:%s created, port:%s listened.", k, srv.connCfg[:9], srv.connCfg[10:]))
				if conn, err := ln.Accept(); err != nil {
					log.Fatal(srv.connCfg, " conn create fatal, errmsg=", err)
				} else {
					srv.conn = conn
					defer srv.conn.Close()
					log.Println("Accepted an access request from cli:", srv.conn.RemoteAddr(), ".")
					handleMsg(srv)
				}
			}
		}()
	}
	brazilEastSrv, australiaDarwinSrv := <-ch1
	log.Println(brazilEastSrv, australiaDarwinSrv)
}

func handleMsg(srv struct {
	id       int
	timezone string
	connCfg  string
	conn     net.Conn
}) {
	var locPtr, _ = time.LoadLocation(srv.timezone)
	for {
		if _, err := io.WriteString(srv.conn, fmt.Sprintln("Access timezone of ", srv.timezone, time.Now().In(locPtr).String())); err != nil {
			log.Println(err)
		}
		time.Sleep(1 * time.Second)
		ch1 <- srv.id
		break
	}
}
