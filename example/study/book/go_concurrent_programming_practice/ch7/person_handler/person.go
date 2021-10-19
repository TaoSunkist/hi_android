package person_handler

type (
	Person struct {
		Name string
		Addr string
	}
	IPersonHandler interface {
		Batch(origs <-chan Person) <-chan Person
		Save(origs <-chan Person) <-chan byte
		Handler(*Person)
	}
	IPersonHandlerImpl struct{}
)

var A int64 = 0
