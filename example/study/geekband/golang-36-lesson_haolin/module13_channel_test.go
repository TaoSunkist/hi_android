package golang36lessonhaolin

import (
	"fmt"
	"testing"
)

type AnimalCategory struct {
	kingdom string
	phylum  string
	class   string
	order   string
	family  string
	genus   string
	species string
}

func (ac AnimalCategory) String() string {
	return fmt.Sprintf("%s%s%s%s%s%s%s",
		ac.kingdom, ac.phylum, ac.class, ac.order,
		ac.family, ac.genus, ac.species)
}

var category = AnimalCategory{species: "cat"}

func TestModule13Struct(t *testing.T) {
	t.Logf("TestModule13Struct %s", category)
}

type Animal struct {
	scientificName string // 学名。
	AnimalCategory        // 动物基本分类。
}

func (a Animal) Category() string {
	return a.AnimalCategory.String()
}

/*go test -count 1 -v -benchtime 3s -bench . -run .13Strcut1 */
func BenchmarkModule13Struct1(b *testing.B) {

	a1 := Animal{AnimalCategory: category}
	b.Logf("%s", a1)
}

type Cat struct {
	name string
	Animal
}

func (cat Cat) String() string {
	return fmt.Sprintf("%s (category: %s, name: %q)",
		cat.scientificName, cat.Animal.AnimalCategory, cat.name)
}

func BenchmarkModule14Strcut2(b *testing.B) {
	c := Cat{name: "中华田园猫"}
	b.Logf("%s, %s, %q", c.scientificName, c.Animal.AnimalCategory, c.name)
}

/*法的接收者类型必须是某个自定义的数据类型，而且不能是接口类型或接口的指针类型。所谓的值方法，就是接收者类型是非指针的自定义数据类型的方法。*/
func (a *Animal) say() {}
func (a Animal) say1() {}

type CatPointer *Cat

/* error: func (c CatPointer) originSay() {}*/

func BenchmarkModule13Strcut3(b *testing.B) {
	/*-bench=. 表示希望运行所有的基准测试函数, 也可以指定基准测试名字, 如-bench="BenchmarkFormat", 并且支持表达式;
	-benchtime="3s" 表示每个基准测试函数持续3s;
	-benchmem提供每次操作分配内存的次数(如2 allocs/op), 以及每次操作分配的字节数(16B/op).*/
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		b.Logf("%d", i)
	}
}

/*
方法的定义感觉本质上也是一种语法糖形式，其本质就是一个函数，声明中的方法接收者就是函数的第一个入参，在调用时go会把施调变量作为函数的第一
个入参的实参传入，比如 func (t MyType) MyMethod(in int) (out int) 可以看作是 func MyMethod(t Mytype, in int) (out int)
比如 myType.MyMethod(123) 就可以理解成是调用MyMethod(myType, 123)，如果myType是*MyType指针类型，则在调用是会自动进行指针解引用，
实际就是这么调用的 MyMethod(*myType, 123)，这么一理解，值方法和指针方法的区别也就显而易见了。*/
/*空结构体不占用内存空间，但是具有结构体的一切属性，如可以拥有方法，可以写入channel。所以当我们需要使用结构体而又不需要具体属性时可以使用它*/
