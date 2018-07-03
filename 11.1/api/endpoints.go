package api

// endpoints.go contains the endpoint definitions, including per-method request
// and response structs. Endpoints are the binding between the service and
// transport.

import (
	"context"
	stdopentracing "github.com/opentracing/opentracing-go"
	"../db"
	"../users"
)

// Endpoints collects the endpoints that comprise the Service.
func getUserEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "get users")
	span.SetTag("service", "user")
	defer span.Finish()

	req := request.(GetRequest)

	userspan := stdopentracing.StartSpan("users from db", stdopentracing.ChildOf(span.Context()))
	usrs, err := s.GetUsers(req.ID)
	userspan.Finish()
	if req.ID == "" {
		return EmbedStruct{usersResponse{Users: usrs}}, err
	}
	if len(usrs) == 0 {
		if req.Attr == "addresses" {
			return EmbedStruct{addressesResponse{Addresses: make([]users.Address, 0)}}, err
		}
		if req.Attr == "cards" {
			return EmbedStruct{cardsResponse{Cards: make([]users.Card, 0)}}, err
		}
		return users.User{}, err
	}
	user := usrs[0]
	attrspan := stdopentracing.StartSpan("attributes from db", stdopentracing.ChildOf(span.Context()))
	db.GetUserAttributes(&user)
	attrspan.Finish()
	if req.Attr == "addresses" {
		return EmbedStruct{addressesResponse{Addresses: user.Addresses}}, err
	}
	if req.Attr == "cards" {
		return EmbedStruct{cardsResponse{Cards: user.Cards}}, err
	}
	return user, err

}
func cardEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "get cards")
	span.SetTag("service", "user")
	defer span.Finish()
	req := request.(GetRequest)
	cardspan := stdopentracing.StartSpan("addresses from db", stdopentracing.ChildOf(span.Context()))
	cards, err := s.GetCards(req.ID)
	cardspan.Finish()
	if req.ID == "" {
		return EmbedStruct{cardsResponse{Cards: cards}}, err
	}
	if len(cards) == 0 {
		return users.Card{}, err
	}
	return cards[0], err
}
func addressEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "get users")
	span.SetTag("service", "user")
	defer span.Finish()
	req := request.(GetRequest)
	addrspan := stdopentracing.StartSpan("addresses from db", stdopentracing.ChildOf(span.Context()))
	adds, err := s.GetAddresses(req.ID)
	addrspan.Finish()
	if req.ID == "" {
		return EmbedStruct{addressesResponse{Addresses: adds}}, err
	}
	if len(adds) == 0 {
		return users.Address{}, err
	}
	return adds[0], err
}
func userPostEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "post user")
	span.SetTag("service", "user")
	defer span.Finish()
	req := request.(users.User)
	id, err := s.PostUser(req)
	return postResponse{ID: id}, err
}
func addressPostEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "post address")
	span.SetTag("service", "user")
	defer span.Finish()
	req := request.(addressPostRequest)
	id, err := s.PostAddress(req.Address, req.UserID)
	return postResponse{ID: id}, err
}
func cardPostEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "post card")
	span.SetTag("service", "user")
	defer span.Finish()
	req := request.(cardPostRequest)
	id, err := s.PostCard(req.Card, req.UserID)
	return postResponse{ID: id}, err
}
func deleteEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "delete entity")
	span.SetTag("service", "user")
	defer span.Finish()
	req := request.(deleteRequest)
	err := s.Delete(req.Entity, req.ID)
	if err == nil {
		return statusResponse{Status: true}, err
	}
	return statusResponse{Status: false}, err
}
func healthEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "health check")
	span.SetTag("service", "user")
	defer span.Finish()
	health := s.Health()
	return healthResponse{Health: health}, nil
}
func loginEndpoint(s *fixedService, ctx context.Context, request interface{}) (interface{}, error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "login user")
	span.SetTag("service", "user")
	defer span.Finish()
	req := request.(loginRequest)
	u, err := s.Login(req.Username, req.Password)
	return userResponse{User: u}, err
}
func registerEndPoint(s *fixedService, ctx context.Context, request interface{}) (response interface{}, err error) {
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "register user")
	span.SetTag("service", "user")
	defer span.Finish()
	req := request.(registerRequest)
	id, err := s.Register(req.Username, req.Password, req.Email, req.FirstName, req.LastName)
	return postResponse{ID: id}, err
}

type GetRequest struct {
	ID   string
	Attr string
}

type loginRequest struct {
	Username string
	Password string
}

type userResponse struct {
	User users.User `json:"user"`
}

type usersResponse struct {
	Users []users.User `json:"customer"`
}

type addressPostRequest struct {
	users.Address
	UserID string `json:"userID"`
}

type addressesResponse struct {
	Addresses []users.Address `json:"address"`
}

type cardPostRequest struct {
	users.Card
	UserID string `json:"userID"`
}

type cardsResponse struct {
	Cards []users.Card `json:"card"`
}

type registerRequest struct {
	Username  string `json:"username"`
	Password  string `json:"password"`
	Email     string `json:"email"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName"`
}

type statusResponse struct {
	Status bool `json:"status"`
}

type postResponse struct {
	ID string `json:"id"`
}

type deleteRequest struct {
	Entity string
	ID     string
}

type healthRequest struct {
	//
}

type healthResponse struct {
	Health []Health `json:"health"`
}

type EmbedStruct struct {
	Embed interface{} `json:"_embedded"`
}
