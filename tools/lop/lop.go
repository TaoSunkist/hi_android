package lop

import (
	"fmt"
	"os"
	"io"
	"runtime"
	"path"
	"bytes"
	"strconv"
	"time"
	"strings"
)

/**
实现Writer接口，自定义写入
text := "%sforeground %sbold%s %sbackground%s"
fmt.Fprintf(os.Stdout, text, "\x1b[31m", "\x1b[1m", "\x1b[21m", "\x1b[41;32m", "\x1b[0m")
*/
const (
	TraceColor = "\x1b[37m"
	DebugColor = "\x1b[34m"
	InfoColor  = "\x1b[36m"
	WarnColor  = "\x1b[33m"
	ErrorColor = "\x1b[31m"
)

func T(prefix ...interface{}) {
	defLop.Output("[T]", TraceColor, fmt.Sprint(prefix...))
}
func D(prefix ...interface{}) {
	defLop.Output("[D]", DebugColor, fmt.Sprint(prefix...))
}
func I(prefix ...interface{}) {
	defLop.Output("[I]", InfoColor, fmt.Sprint(prefix...))
}
func W(prefix ...interface{}) {
	defLop.Output("[W]", WarnColor, fmt.Sprint(prefix...))
}
func E(prefix ...interface{}) {
	defLop.Output("[E]", ErrorColor, fmt.Sprint(prefix...))
}

var defLop = new(os.Stderr)

func new(out io.Writer) *DefLop {
	projectPath, err := os.Getwd()
	if err != nil {
		panic(err)
		return nil
	}
	fmt.Println(projectPath)
	logFile, err := os.OpenFile(fmt.Sprint(projectPath+"/"+projectPath[strings.LastIndex(projectPath, "/")+1:]+".log"), os.O_CREATE|os.O_APPEND|os.O_WRONLY, 0777)
	if err != nil {
		panic(err)
		return nil
	}
	return &DefLop{out, logFile, nil}
}

type DefLop struct {
	out     io.Writer   //日志的写入拦截
	logFile *os.File    //日志的写入路径
	msg     chan string //日志的信息队列
}

func (d *DefLop) getOutputMetaInfo() (string, string) {
	_, file, line, ok := runtime.Caller(3)
	if !ok {
		file = "???"
		line = 0
	}
	_, filename := path.Split(file)
	return filename, strconv.Itoa(line)
}

func (d *DefLop) Output(levelTag, levelColor, msg string) (n int, err error) {
	filename, line := d.getOutputMetaInfo()
	msgBuf := bytes.NewBufferString(levelColor)

	msgBuf.WriteString(fmt.Sprintf("%s  %s %s:%s ",
		levelTag,
		time.Now().Format("2006-01-02 15:04:05 999"),
		filename,
		line))
	msgBuf.WriteString(msg)
	msgBuf.WriteString(" \x1b[0m")
	msgBuf.WriteString("\n")
	os.Stderr.Write(msgBuf.Bytes())
	d.logFile.Sync()
	return d.logFile.Write(msgBuf.Bytes())
}
