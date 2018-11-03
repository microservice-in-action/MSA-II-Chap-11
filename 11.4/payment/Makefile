INSTANCE = payment-service

default: build

pre: 
	go get -v github.com/Masterminds/glide

deps: pre
	glide install

rm-deps:
	rm -rf vendor

cover:
	@glide novendor|xargs go test -v -covermode=count

coverprofile:
	go get github.com/modocache/gover
	go test -v -covermode=count -coverprofile=profile.coverprofile
	go test -v -covermode=count -coverprofile=db.coverprofile ./db
	go test -v -covermode=count -coverprofile=mongo.coverprofile ./db/mongodb
	go test -v -covermode=count -coverprofile=api.coverprofile ./api
	go test -v -covermode=count -coverprofile=users.coverprofile ./users
	gover
	mv gover.coverprofile cover.profile
	rm *.coverprofile


docker: build
	docker build -t $(INSTANCE) .

cleandocker:
	-docker rm -f $(INSTANCE)

clean: cleandocker 
	rm -rf bin
	rm -rf vendor

build: deps
	mkdir -p bin
	GOOS=linux GOARCH=amd64 CGO_ENABLED=0 go build -a -installsuffix cgo -o bin/$(INSTANCE) main.go

buildmac: deps
	mkdir -p bin
	CGO_ENABLED=0 go build -a -installsuffix cgo -o bin/$(INSTANCE) main.go
