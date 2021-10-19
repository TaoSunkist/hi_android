package ch5

import (
	"testing"
	"time"
)

/*
goos: darwin
goarch: amd64
pkg: hi_golang/book/go_concurrent_programming_practice/ch5
BenchmarkTest1-8        2000000000               0.00 ns/op
	测试目标				 运行次数				   平均每秒运行时间
--- BENCH: BenchmarkTest1-8
        benchmark_test.go:10: TestBenchmark
        benchmark_test.go:10: TestBenchmark
        benchmark_test.go:10: TestBenchmark
        benchmark_test.go:10: TestBenchmark
        benchmark_test.go:10: TestBenchmark
        benchmark_test.go:10: TestBenchmark
PASS
ok      hi_golang/book/go_concurrent_programming_practice/ch5   5.014s
*/
func BenchmarkTest1(b *testing.B) {
	b.Log("TestBenchmark")
}

func BenchmarkSkipCodeBlock(b *testing.B) {
	b.Log("TestBenchmark")
	isSkip := false
	if isSkip {
		// b.StopTimer() 停止計時測試。 這可用於在執行複雜的初始化時暫停計時器，而不需要進行測量。
		b.StopTimer()
	}
	//下面这行代码消耗的时间将不会纳入banchmark的性能测试中
	time.Sleep(time.Second)
	b.StartTimer()
}
