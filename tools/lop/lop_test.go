package lop

import (
	"os"
	"testing"
	"log"
	"path/filepath"
	"strings"
)

func TestLop(t *testing.T) {
	//W("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

	T("trace ...")
	D("debug ...")
	I("info  ...")
	W("warn  ...")
	E("error ...")

	log.Println("trace ...")
	t.Log(filepath.Abs("./"))
	projectPath, err := os.Getwd()
	t.Log(projectPath, err)
	t.Log(projectPath[strings.LastIndex(projectPath, "/")+1:], err)
}

func BenchmarkLop(b *testing.B) {
	T("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
}
