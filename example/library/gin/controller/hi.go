package controller

import (
	"fmt"
	"github.com/gin-gonic/gin"
	"net/http"
)

func GoBattery(c *gin.Context) {
	c.HTML(http.StatusOK, "go_battery.html", gin.H{})
}
func VerificationPostParams(c *gin.Context) {
	fmt.Println("VerificationPostParams", c.Request.Form)

	for k, v := range c.Request.PostForm {
		fmt.Println(k, v)
	}
}