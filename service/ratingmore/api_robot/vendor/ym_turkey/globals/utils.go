package globals

import (
	"hi_golang/tools/lop"
	"bytes"
	"strconv"
	"math"
	"time"
)

func FormatArgs2Map(args ...interface{}) map[string]interface{} {
	if len(args) == 0 {
		return nil
	}

	args = args[0].([]interface{})
	argsLen := len(args)
	if argsLen%2 != 0 {
		return nil
	}
	argMap := make(map[string]interface{}, argsLen/2)
	lop.T(args)
	for index, v := range args {
		if index%2 == 0 {
			valueIndex := index + 1
			vStr := v.(string)
			argMap[vStr] = args[valueIndex]
		} else {
			continue
		}
	}
	return argMap
}

func Decimal(zero int64) float64 {
	btsBuf := bytes.NewBufferString("")
	btsBuf.WriteString("1")
	for ; zero > 0; zero-- {
		btsBuf.WriteString("0")
	}
	a, _ := strconv.ParseFloat(btsBuf.String(), 64)
	return a
}

/*
BTC 地址数据所在的分表位置1~20
 */
func GetBtcTableID(str string) int { return int(math.Abs(float64(mixHash(str) % 20))) }

func mixHash(str string) int64 {
	var h = hash(str)
	h1 := int64(h)
	h1 <<= 32
	h1 |= fNVHash_v1(str)
	return h1
}

func fNVHash_v1(data string) int64 {
	var p int32 = 16777619
	var hash int32 = -2128831035
	dataLen := len(data)
	for i := 0; i < dataLen; i++ {
		hash = (hash ^ rune(data[i])) * p
	}

	hash += hash << 13
	hash ^= hash >> 7
	hash += hash << 3
	hash ^= hash >> 17
	hash += hash << 5
	return int64(hash)
}

func hash(s string) int32 {
	h := int32(0)
	for i := 0; i < len(s); i++ {
		h = 31*h + int32(s[i])
	}
	return h
}

//------------------------------------------------------------------------------------------------

/*
 time.Unix(sec, 0).Format("2006-01-02 15:04:05")
 */
func UnixFmt(sec int64) string {
	if sec != 0 {
		return time.Unix(sec, 0).Format("2006-01-02 15:04:05")
	} else {
		return "-"
	}
}
