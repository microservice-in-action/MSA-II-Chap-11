package payment

import (
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"context"
	rf "github.com/ServiceComb/go-chassis/server/restful"
	"encoding/json"
)

// MakeHTTPHandler mounts the endpoints into a REST-y HTTP handler.

func (s *loggingMiddleware) URLPatterns() []rf.RouteSpec {
	return []rf.RouteSpec{
		{http.MethodPost, "/paymentAuth", "AuthoriseEndpoint"},
		{http.MethodGet, "/health", "HealthEndpoint"},
		{http.MethodGet,"/coupon/{couponid}","ApplyCouponIdEndpoint"},
	}
}
func(s *loggingMiddleware)ApplyCouponIdEndpoint(b *rf.Context){
	ctx := context.Background()
	couponReq,err := decodeCouponIdRequest(ctx,b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := applyCouponIdEndpoint(s,ctx,couponReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeCouponIdResponse(ctx,b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}

}
func(s *loggingMiddleware)AuthoriseEndpoint(b *rf.Context){
	ctx := context.Background()
	authReq,err := decodeAuthoriseRequest(ctx,b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := authoriseEndpoint(s,ctx,authReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeAuthoriseResponse(ctx,b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}

}
func(s *loggingMiddleware)HealthEndpoint(b *rf.Context){
	ctx := context.Background()
	healthReq,err := decodeHealthRequest(ctx,b)
	if err != nil{
		encodeError(err,b)
		return
	}
	resp,err := healthEndpoint(s,ctx,healthReq)
	if err != nil{
		encodeError(err,b)
		return
	}
	err = encodeHealthResponse(ctx,b,resp)
	if err != nil{
		encodeError(err,b)
		return
	}
}
func encodeError( err error, w *rf.Context) {
	code := http.StatusInternalServerError
	body := map[string]interface{}{
		"error":       err.Error(),
		"status_code": code,
		"status_text": http.StatusText(code),
	}
	w.WriteHeaderAndJSON(code,body,"application/json")
}

func decodeCouponIdRequest(_ context.Context, r *rf.Context) (interface{}, error) {
	return ApplyCouponRequest{
		CouponId: r.ReadPathParameter("couponid"),
	},nil
}
func decodeAuthoriseRequest(_ context.Context, r *rf.Context) (interface{}, error) {
	// Read the content
	var bodyBytes []byte
	if r.ReadRequest().Body != nil {
		var err error
		bodyBytes, err = ioutil.ReadAll(r.ReadRequest().Body)
		if err != nil {
			return nil, err
		}
	}
	// Save the content
	bodyString := string(bodyBytes)
	// Decode auth request
	var request AuthoriseRequest = AuthoriseRequest{}
	if err :=json.Unmarshal(bodyBytes,&request); err != nil {
		return nil, err
	}

	// If amount isn't present, error
	if request.Amount == 0.0 {
		return nil, &UnmarshalKeyError{
			Key:  "amount",
			JSON: bodyString,
		}
	}
	return request, nil
}

type UnmarshalKeyError struct {
	Key  string
	JSON string
}

func (e *UnmarshalKeyError) Error() string {
	return fmt.Sprintf("Cannot unmarshal object key %q from JSON: %s", e.Key, e.JSON)
}

var ErrInvalidJson = errors.New("Invalid json")

func encodeAuthoriseResponse(ctx context.Context, w *rf.Context, response interface{}) error {
	resp := response.(AuthoriseResponse)
	if resp.Err != nil {
		encodeError(resp.Err, w)
		return nil
	}
	return encodeResponse(ctx, w, resp.Authorisation)
}

func decodeHealthRequest(_ context.Context, r *rf.Context) (interface{}, error) {
	return struct{}{}, nil
}
func encodeCouponIdResponse(ctx context.Context, w *rf.Context, response interface{}) ( error) {
	resp := response.(ApplyCouponResponse)
	return encodeResponse(ctx,w,resp.CouponStatus)
}
func encodeHealthResponse(ctx context.Context, w *rf.Context, response interface{}) error {
	return encodeResponse(ctx, w, response.(healthResponse))
}

func encodeResponse(_ context.Context, w *rf.Context, response interface{}) error {
	// All of our response objects are JSON serializable, so we just do that.
	 return w.WriteJSON(response,"application/json")
}
