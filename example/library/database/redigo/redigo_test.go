package redigo

import (
	"github.com/gomodule/redigo/redis"
	"reflect"
	"testing"
)

func TestRedigoQuery(t *testing.T) {
	t.Log("TestRedis")
	conn, err := redis.Dial("tcp", "192.168.116.129:6379")
	if err != nil {
		t.Log(err)
	}
	defer conn.Close()
	keys := []interface{}{"A", "B", "C"}
	t.Log(keys)
	values, err := redis.Values(conn.Do("MGET", keys...))
	if err != nil {
		t.Fatal(err)
	}
	for _, elem := range values {
		if elemStringBytes, ok := elem.([]byte); ok {
			t.Log(string(elemStringBytes))
		} else {
			t.Error(elemStringBytes, elem, ok)
		}
	}
}

func BenchmarkRedigoInsert(b *testing.B) {
	conn, err := redis.Dial("tcp", "192.168.116.129:6379")
	if err != nil {
		b.Log(err)
	}
	redis.String(conn.Do("SET", "D", "d"))
	b.Log(redis.String(conn.Do("SET", "D", "d")))
}

func TestRedisDT(t *testing.T) {
	conn, _ := redis.Dial("tcp", "192.168.116.129:6379")
	reply, e := conn.Do("set", "A", "a")
	t.Log(reflect.TypeOf(reply), reply, e)
	reply, e = conn.Do("del", "A")
	t.Log(reflect.TypeOf(reply), reply, e)
	reply, e = conn.Do("hset", "hash-key", "sub-key1", "value1", "sub-key2", "value2")
	//reply1, e := conn.Do("lpush", "A", "a", "a1", "a2")
	//t.Log(reply1, e)
}
