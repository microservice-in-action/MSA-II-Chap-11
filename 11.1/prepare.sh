#!/bin/bash

mkdir -p $GOPATH/src/github.com/ServiceComb
cd $GOPATH/src/github.com/ServiceComb
git clone https://github.com/ServiceComb/go-chassis.git
glide install

go get github.com/emicklei/go-restful
go get github.com/opentracing/opentracing-go
go get gopkg.in/mgo.v2
go get gopkg.in/mgo.v2/bson
