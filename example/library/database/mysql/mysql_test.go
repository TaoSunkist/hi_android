package main

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"testing"
	"log"
	"time"
	"sync"
)

func init1() {

	var testDBConnPool = sync.Pool{}
	db1, err := sql.Open("mysql", "root:taohui@tcp(192.168.116.129:3306)/test")
	db2, err := sql.Open("mysql", "root:taohui@tcp(192.168.116.129:3306)/test")
	db3, err := sql.Open("mysql", "root:taohui@tcp(192.168.116.129:3306)/test")
	log.Println("创建数据库链接, err: ", err)
	testDBConnPool.Put(db1)
	testDBConnPool.Put(db2)
	testDBConnPool.Put(db3)
}

//测试mysql插入时索引的速度影响
func TestMysqlIst(t *testing.T) {
	t.Log("TestMysqlIst")
	//dbConn, _ := sql.Open("mysql", "root:taohui@tcp(192.168.116.129:3306)/test")
	//t.Log(dbConn)
	tn := time.Now()
	taskCh := make(chan int)

	//for i := 0; i <= 10000000; i++ {
	//	rows, _ := dbConn.Query(fmt.Sprintf(`insert into person (name,age) values ('%s',%d);`, fmt.Sprintf("0_%d", i), i))
	//	rows.Close()
	//}
	t.Log("total duration: ", time.Now().Sub(tn), ", ", <-taskCh)
}
