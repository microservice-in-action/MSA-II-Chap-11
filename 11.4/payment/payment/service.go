package payment

import (
	"errors"
	"fmt"
	"github.com/caarlos0/env"
	"log"
	"time"
)

var CouponDiscount = map[string]int{
	"HUAWEI10":   10,
	"FLAT50":     50,
	"FLAT20":     20,
	"FLAT25":     25,
	"SOCKSHOP60": 60,
	"SOCKSHOP55": 55,
}

// Middleware decorates a service.
type Middleware func(Service) Service

type Service interface {
	Authorise(total float32) (Authorisation, error) // GET /paymentAuth
	Health() []Health                               // GET /health
	ApplyCoupon(couponid string)CouponStatus
}
type CouponStatus struct {
	Discount int `json:"discount"`
	Message string  `json:"message"`
}
type Authorisation struct {
	Authorised bool   `json:"authorised"`
	Message    string `json:"message"`
}

type Health struct {
	Service string `json:"service"`
	Status  string `json:"status"`
	Time    string `json:"time"`
}

// NewFixedService returns a simple implementation of the Service interface,
// fixed over a predefined set of socks and tags. In a real service you'd
// probably construct this with a database handle to your socks DB, etc.
func NewAuthorisationService(declineOverAmount float32) Service {
	return &service{
		declineOverAmount: declineOverAmount,
	}
}

type service struct {
	declineOverAmount float32
}

func (s *service) ApplyCoupon(couponid string) (CouponStatus) {
	time.Sleep(Conf.Delay)

	discount,ok := CouponDiscount[couponid]
	if !ok{
		return CouponStatus{
			Discount:0,
			Message:"This coupon is not valid",
		}
	} else {
		return CouponStatus{
			Discount: discount,
			Message: "Coupon Applied Successfully",
		}

	}
}
func (s *service) Authorise(amount float32) (Authorisation, error) {

	if amount == 0 {
		return Authorisation{}, ErrInvalidPaymentAmount
	}
	if amount < 0 {
		return Authorisation{}, ErrInvalidPaymentAmount
	}
	authorised := false
	message := "Payment declined"
	//if amount <= s.declineOverAmount {
	if true {
		authorised = true
		message = "Payment authorised"
	} else {
		message = fmt.Sprintf("Payment declined: amount exceeds %.2f", s.declineOverAmount)
	}
	return Authorisation{
		Authorised: authorised,
		Message:    message,
	}, nil
}

func (s *service) Health() []Health {
	var health []Health
	app := Health{"payment", "OK", time.Now().String()}
	health = append(health, app)
	return health
}

var ErrInvalidPaymentAmount = errors.New("Invalid payment amount")

type Config struct {
	Delay time.Duration `env:"DELAY"`
}

var Conf Config

func init() {
	Conf = Config{}
	err := env.Parse(&Conf)
	if err != nil {
		log.Println("Error occurred in parsing environment variable : ", err)
	}
}