package main

import (
	"fmt"
	"os"
	"os/signal"
	"syscall"

	"github.com/ServiceComb/go-chassis/core/lager"
	"./api"
	"./db"
	"./db/mongodb"
	corelog "log"
	//"code.huawei.com/cse/go-chassis"
	"github.com/ServiceComb/go-chassis/core/registry"
	_ "github.com/ServiceComb/go-chassis/server/restful"
	// _ "github.com/ServiceComb/go-chassis/third_party/forked/go-micro/server/highway"
	// _ "github.com/ServiceComb/go-chassis/third_party/forked/go-micro/transport/tcp"
	//"github.com/ServiceComb/go-chassis/core/server"
	"github.com/ServiceComb/go-chassis"
	"./users"
)

const (
	ServiceName = "user"
)

func init() {

	db.Register("mongodb", &mongodb.Mongo{})
}

func main() {

	err := chassis.Init()
	if err != nil {
		corelog.Panicln(err)
	}
	registry.Enable()
	// Mechanical stuff.
	errc := make(chan error)
	users.Init()
	// Log domain.
	dbconn := false
	for !dbconn {
		err := db.Init()
		if err != nil {
			if err == db.ErrNoDatabaseSelected {
				corelog.Fatal(err)
			}
			corelog.Print(err)
		} else {
			dbconn = true
		}
	}

	var service api.Service
	service = api.NewFixedService()

	//_, err = chassis.RegisterSchema("rest", service, server.WithSchemaId("UserServer"), server.WithMicroServiceName(ServiceName))
	chassis.RegisterSchema("rest", service)
	if err != nil {
		lager.Logger.Errorf(err, "UserServer start failed.")
	}
	chassis.Run()

	// Capture interrupts.
	go func() {
		c := make(chan os.Signal)
		signal.Notify(c, syscall.SIGINT, syscall.SIGTERM)
		errc <- fmt.Errorf("%s", <-c)
	}()

	lager.Logger.Error("exit", <-errc)
}
