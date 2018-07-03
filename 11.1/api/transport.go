package api

// transport.go contains the binding from endpoints to a concrete transport.
// In our case we just use a REST-y HTTP transport.

import (
	"context"
	"errors"
	"net/http"
	"strings"
	"../users"
	rf "github.com/ServiceComb/go-chassis/server/restful"
	"github.com/emicklei/go-restful/log"
)

var (
	ErrInvalidRequest = errors.New("Invalid request")
)

// MakeHTTPHandler mounts the endpoints into a REST-y HTTP handler.

func (s *fixedService) URLPatterns() []rf.RouteSpec {
	return []rf.RouteSpec {
		{http.MethodGet, "/login", "LoginEndpoint"},
		{http.MethodPost,"/register", "RegisterEndpoint"},
		{http.MethodGet, "/customers", "UserGetEndpoint"},
		{http.MethodGet, "/customers/{id}", "UserWithIDGetEndpoint"},
		{http.MethodGet, "/customers/{id}/cards", "UserWithIDAndCardGetEndpoint"},
		{http.MethodGet, "/customers/{id}/addresses", "UserWithIDAndAddressesGetEndpoint"},
		{http.MethodGet, "/cards", "CardGetEndpoint"},
		{http.MethodGet, "/cards/{id}", "CardWithIDGetEndpoint"},
		{http.MethodGet, "/addresses", "AddressGetEndpoint"},
		{http.MethodGet, "/addresses/{id}", "AddressWithIDGetEndpoint"},
		{http.MethodPost,"/customers", "UserPostEndpoint"},
		{http.MethodPost,"/addresses", "AddressPostEndpoint"},
		{http.MethodPost,"/cards", "CardPostEndpoint"},
		{http.MethodDelete,"/addresses/{id}", "DeleteEndpoint"},
		{http.MethodDelete,"/customers/{id}", "DeleteEndpoint"},
		{http.MethodDelete,"/cards/{id}", "DeleteEndpoint"},
		{http.MethodGet, "/health", "HealthEndpoint"},

	}
}

func (s *fixedService)LoginEndpoint(b *rf.Context){
	ctx := context.TODO()
	loginReq,err := decodeLoginRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := loginEndpoint(s,ctx,loginReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}

}

func(s *fixedService)RegisterEndpoint(b *rf.Context){
	ctx := context.TODO()
	regReq,err := decodeRegisterRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := registerEndPoint(s,ctx,regReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)UserGetEndpoint(b *rf.Context){
	ctx := context.TODO()
	getReq,err := decodeGetRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := getUserEndpoint(s,ctx,getReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}

}
func(s *fixedService)UserWithIDGetEndpoint(b *rf.Context){
	ctx := context.TODO()
	getReq,err := decodeGetRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := getUserEndpoint(s,ctx,getReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)UserWithIDAndCardGetEndpoint(b *rf.Context){
	ctx := context.TODO()
	getReq,err := decodeGetRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := getUserEndpoint(s,ctx,getReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)UserWithIDAndAddressesGetEndpoint(b *rf.Context){
	ctx := context.TODO()
	getReq,err := decodeGetRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := getUserEndpoint(s,ctx,getReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)CardGetEndpoint(b *rf.Context){
	ctx := context.TODO()
	getReq,err := decodeGetRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := cardEndpoint(s,ctx,getReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}

}
func(s *fixedService)CardWithIDGetEndpoint(b *rf.Context){
	ctx := context.TODO()
	getReq,err := decodeGetRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := cardEndpoint(s,ctx,getReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)AddressGetEndpoint(b *rf.Context){
	ctx := context.TODO()
	getReq,err := decodeGetRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := addressEndpoint(s,ctx,getReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)AddressWithIDGetEndpoint(b *rf.Context){
	ctx := context.TODO()
	getReq,err := decodeGetRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := addressEndpoint(s,ctx,getReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)UserPostEndpoint(b *rf.Context){
	ctx := context.TODO()
	usrReq,err := decodeUserRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := userPostEndpoint(s,ctx,usrReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)AddressPostEndpoint(b *rf.Context){
	ctx := context.TODO()
	addrPostReq,err := decodeAddressRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := addressPostEndpoint(s,ctx,addrPostReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}

}
func(s *fixedService)CardPostEndpoint(b *rf.Context){
	ctx := context.TODO()
	cardReq,err := decodeCardRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := cardPostEndpoint(s,ctx,cardReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)DeleteEndpoint(b *rf.Context){
	ctx := context.TODO()
	delReq,err := decodeDeleteRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := deleteEndpoint(s,ctx,delReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func(s *fixedService)HealthEndpoint(b *rf.Context){
	ctx := context.TODO()
	healthReq,err := decodeHealthRequest(b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := healthEndpoint(s,ctx,healthReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeHealthResponse(b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func encodeError( err error, w *rf.Context) {
	code := http.StatusInternalServerError
	switch err {
	case ErrUnauthorized:
		code = http.StatusUnauthorized
	}
	body := map[string]interface{}{
		"error":       err.Error(),
		"status_code": code,
		"status_text": http.StatusText(code),
	}
	w.WriteHeaderAndJSON(code,body,"application/hal+json")
}

func decodeLoginRequest( r *rf.Context) (interface{}, error) {
	u, p, ok := r.ReadRequest().BasicAuth()
	if !ok {
		return loginRequest{}, ErrUnauthorized
	}
	log.Print("decoding of Login request is Sucessfull : Decoded Username and Password is ",u,"|",p)
	return loginRequest{
		Username: u,
		Password: p,
	}, nil
}

func decodeRegisterRequest( r *rf.Context) (interface{}, error) {
	reg := registerRequest{}
	err := r.ReadEntity(&reg)
	if err != nil {
		return nil, err
	}
	return reg, nil
}

func decodeDeleteRequest( r *rf.Context) (interface{}, error) {
	d := deleteRequest{}
	u := strings.Split(r.ReadRequest().URL.Path, "/")
	if len(u) == 3 {
		d.Entity = u[1]
		d.ID = u[2]
		return d, nil
	}
	return d, ErrInvalidRequest
}

func decodeGetRequest( r *rf.Context) (interface{}, error) {
	g := GetRequest{}
	u := strings.Split(r.ReadRequest().URL.Path, "/")
	if len(u) > 2 {
		g.ID = u[2]
		if len(u) > 3 {
			g.Attr = u[3]
		}
	}
	return g, nil
}

func decodeUserRequest( r *rf.Context) (interface{}, error) {
	defer r.ReadRequest().Body.Close()
	u := users.User{}
	err := r.ReadEntity(&u)
	if err != nil {
		return nil, err
	}
	return u, nil
}

func decodeAddressRequest( r *rf.Context) (interface{}, error) {
	defer r.ReadRequest().Body.Close()
	a := addressPostRequest{}
	err := r.ReadEntity(&a)
	if err != nil {
		return nil, err
	}
	return a, nil
}

func decodeCardRequest( r *rf.Context) (interface{}, error) {
	defer r.ReadRequest().Body.Close()
	c := cardPostRequest{}
	err := r.ReadEntity(&c)
	if err != nil {
		return nil, err
	}
	return c, nil
}

func decodeHealthRequest( r *rf.Context) (interface{}, error) {
	return struct{}{}, nil
}

func encodeHealthResponse( w *rf.Context, response interface{}) error {
	return encodeResponse(w, response.(healthResponse))
}

func encodeResponse(w *rf.Context, response interface{}) error {
	// All of our response objects are JSON serializable, so we just do that.
	return w.WriteJSON(response,"application/hal+json")
}
