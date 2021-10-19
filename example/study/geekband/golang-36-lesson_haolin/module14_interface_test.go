package golang36lessonhaolin

import (
	"fmt"
	"testing"
)

type animal interface {
	setLanguage()
	setFavorite()
}

type personTrait interface {
	/*标示类型*/
	ipersion()
}

type taohui struct {
	language string
	favorite string
}

var t1 taohui

func init() {
	t1 = taohui{language: "China", favorite: "PlayGame"}
	fmt.Println("init(), ", t1)
}

func (t *taohui) setLanguage(language string) {
	t.language = language
}

func (taohui) setFavorite() {}

func (taohui) ipersion() {}

func TestInterface(t *testing.T) {
	//var p = t1
	var p1 personTrait = taohui{language: "China1", favorite: "Playgame1"}
	t.Log(p1)
	var p2 personTrait = new(taohui)
	t.Log(p2)
	t.Log(p2)
}

/*------------*/
type WardrobeTrait interface {
	hasDoor()
}

type WhiteWardrobe struct {
}

func (*WhiteWardrobe) hasDoor() {}

func TestWardrobe(t *testing.T) {
	var whiteWardrobe *WhiteWardrobe
	whiteWardrobe2 := whiteWardrobe
	var wardrobeTrait WardrobeTrait
	wardrobeTrait = whiteWardrobe2
	t.Log(wardrobeTrait, wardrobeTrait == nil)

}
