package payment

import (
	stdopentracing "github.com/opentracing/opentracing-go"
	"context"
)

// Endpoints collects the endpoints that comprise the Service.
func authoriseEndpoint(s Service,ctx context.Context,request interface{})( interface{}, error){

	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "authorize payment")
	span.SetTag("service", "payment")
	defer span.Finish()
	req := request.(AuthoriseRequest)
	authorisation, err := s.Authorise(req.Amount)
	return AuthoriseResponse{Authorisation: authorisation, Err: err}, nil

}
func applyCouponIdEndpoint(s Service,ctx context.Context,request interface{})( interface{}, error){
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "authorize payment")
	span.SetTag("service", "payment")
	defer span.Finish()
	req := request.(ApplyCouponRequest)
	couponStatus := s.ApplyCoupon(req.CouponId)
	return ApplyCouponResponse{CouponStatus:couponStatus,Err:nil},nil
}
func healthEndpoint(s Service,ctx context.Context,request interface{})( interface{}, error){
	var span stdopentracing.Span
	span, ctx = stdopentracing.StartSpanFromContext(ctx, "health check")
	span.SetTag("service", "payment")
	defer span.Finish()
	health := s.Health()
	return healthResponse{Health: health}, nil
}
// AuthoriseRequest represents a request for payment authorisation.
// The Amount is the total amount of the transaction
type AuthoriseRequest struct {
	Amount float32 `json:"amount"`
}

// AuthoriseResponse returns a response of type Authorisation and an error, Err.
type AuthoriseResponse struct {
	Authorisation Authorisation
	Err           error
}

type healthRequest struct {
	//
}

type healthResponse struct {
	Health []Health `json:"health"`
}

type ApplyCouponRequest struct{
	CouponId string  `json:"id"`
}

type ApplyCouponResponse struct{
	CouponStatus CouponStatus
	Err 		error
}
