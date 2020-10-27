package other

import (
	"testing"
	"time"
	"strconv"
	"bytes"
	"github.com/jinzhu/gorm"
)

func TestTimeUnixExpire(t *testing.T) {
	curTimeStamp := time.Now().Unix()
	//t.Log(curTimeStamp)
	//curTime := time.Unix(curTimeStamp, 0)
	//time.Sleep(4 * time.Second)
	//t.Log(time.Now().Before(curTime.Add(5 * time.Second))) //如果在五秒中之内=true，否则false
	curTimeStampStr := strconv.FormatInt(curTimeStamp, 10)
	t.Log(strconv.FormatFloat(1.525956051658e+12, 'e', 0, 64))
	t.Log(strconv.ParseInt(curTimeStampStr, 10, 64))
	var a names
	a = append(a, "1")
	a = append(a, "1")
	a = append(a, "1")
	a = append(a, "1")
	t.Logf("%s", a)
	t.Log(a)

}

type names []string

func (n names) String() string {
	var bytesBuf *bytes.Buffer
	if len(n) == 0 {
		bytesBuf = bytes.NewBufferString(n[0])
	} else {
		bytesBuf = bytes.NewBufferString(n[0])
		for _, n1 := range n {
			bytesBuf.WriteString(",")
			bytesBuf.WriteString(n1)
		}
	}
	return bytesBuf.String()
}

func TestTime1(t *testing.T) {
	t1, _ := time.Parse("2006-01-02", "2018-02-03")
	t2, _ := time.Parse("2006-01-02", "2018-09-03")
	for t1.Before(t2) {
		t1 = t1.AddDate(0, 0, 1)
		t.Log("......", t1)
	}
}

func TestTime2(t *testing.T) {
	var klineDao struct {
		gorm.Model
		Timestamp int64 `gorm:"PRIMARY_KEY"`
		Date      *time.Time
		Open      float64
		Close     float64
		Low       float64
		High      float64
		Volume    float64
		Tag       string
	}
	time12, _ := time.Parse("2006-01-02", "2018-07-04")
	time13, _ := time.Parse("2006-01-02", "2018-07-06")
	nowTime := time.Now()
	//0705是否在0704之后
	t.Log(nowTime.After(time12))
	//0705是否在0706之前
	t.Log(nowTime.Before(time13))
	t.Log(klineDao.Timestamp)
}
