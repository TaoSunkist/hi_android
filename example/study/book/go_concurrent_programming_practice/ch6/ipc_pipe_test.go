package ch6

import (
	"testing"
	"os/exec"
	"bytes"
	"io"
	"bufio"
)

func TestIpcPipe(t *testing.T) {
	t.Log("TestIpcPipe")

	cmd := exec.Command("echo", "-n", "My first command from golang.")

	stdoutPipe, err := cmd.StdoutPipe() //返回一个输出管道，Linux的标准输入输出流：0: stdin,1: stdout,2: stderr
	if err != nil {
		t.Fatal("Error: ", err)
	}
	defer stdoutPipe.Close()

	if err := cmd.Start(); err != nil {
		t.Fatal(err)
	}

	stdoutPipeResultBytes := make([]byte, 29)
	if n, err := stdoutPipe.Read(stdoutPipeResultBytes); err != nil {
		t.Fatal(err)
	} else {
		t.Log(n, string(stdoutPipeResultBytes))
	}
}

//使用字节缓冲区循环从stdout标准输出流中读取数据
func TestIpcPipe1(t *testing.T) {
	t.Log("TestIpcPipe")

	cmd := exec.Command("echo", "-n", "My first command from golang.")

	stdoutPipe, err := cmd.StdoutPipe() //返回一个输出管道，Linux的标准输入输出流：0: stdin,1: stdout,2: stderr
	if err != nil {
		t.Fatal("Error: ", err)
	}

	defer stdoutPipe.Close()
	if err := cmd.Start(); err != nil {
		t.Fatal(err)
	}

	stdoutPipeResultBytesBuf := bytes.Buffer{}
	for {
		stdoutPipeResultBytes := make([]byte, 5)
		n, err := stdoutPipe.Read(stdoutPipeResultBytes)
		//如果command的stdout1输出小于stdoutPipeResultBytes的len，n：代表实际的输出长度
		if err != nil {
			if err == io.EOF {
				t.Error(err)
				break
			} else {
				//TODO 从stdout1输出流中读取出错处理
				t.Fatal("read bytes from studoutPipe error: ", err)
			}
		}
		if n > 0 {
			//在考虑错误之前，调用者应始终处理返回的n > 0个字节。这样做可以正确处理读取一些字节后发生的I / O错误以及两种允许的EOF行为。
			stdoutPipeResultBytesBuf.Write(stdoutPipeResultBytes)
		}
	}
	t.Log(stdoutPipeResultBytesBuf.String())
}

//直接使用带缓冲的读取器从stdout1输出流中读取数据
func TestIpcPipe2(t *testing.T) {
	cmd := exec.Command("echo", "-n", "My first command from golang...")
	stdoutPipe, err := cmd.StdoutPipe()
	if err != nil {
		t.Fatal(err)
	}
	if err := cmd.Start(); err != nil {
		t.Fatal(err)
	}
	//该缓冲读取器会自带一个4096长度的缓冲区
	outputBuf0 := bufio.NewReader(stdoutPipe)
	//line: 代表一行的数据，isPrefix：代表此行是否读完
	line, isPrefix, err := outputBuf0.ReadLine()
	if err != nil {
		t.Fatal(err)
	} else {
		t.Log("isPrefix:", isPrefix, string(line))
	}
}

func TestIpcPipe3(t *testing.T) {
	t.Log("exec command: ps aux | grep idea")

	cmd := exec.Command("echo", "taohui\ntianyu\ntiaohui")
	cmdStdoutPipe, err := cmd.StdoutPipe()

	if err != nil {
		t.Fatal(err)
	}

	if err := cmd.Start(); err != nil {
		t.Fatal(err)
	}

	cmd1 := exec.Command("grep", "ti")

	cmd1.Stdin = cmdStdoutPipe
	cmd1StdoutPipe, err := cmd1.StdoutPipe()
	if err := cmd1.Start(); err != nil {
		t.Fatal(err)
	}
	t.Log(bufio.NewReader(cmd1StdoutPipe).ReadLine())
	t.Log(bufio.NewReader(cmd1StdoutPipe).ReadLine())

}
