package payment

import (
	"github.com/emicklei/go-restful/log"
	"time"
)

// LoggingMiddleware logs method calls, parameters, results, and elapsed time.
func LoggingMiddleware(logger log.StdLogger, next Service) Service {
	return &loggingMiddleware{
		next:   next,
		logger: logger,
	}
}

type loggingMiddleware struct {
	next   Service
	logger log.StdLogger
}

func (mw *loggingMiddleware) Authorise(amount float32) (auth Authorisation, err error) {
	defer func(begin time.Time) {
		mw.logger.Print("\n",
			"method\t", "Authorise","\n",
			"result\t", auth.Authorised,"\n",
			"took\t", time.Since(begin),"\n",
		)
	}(time.Now())
	return mw.next.Authorise(amount)
}

func (mw *loggingMiddleware) Health() (health []Health) {
	defer func(begin time.Time) {
		mw.logger.Print("\n",
			"method\t", "Health","\n",
			"result\t", len(health),"\n",
			"took\t", time.Since(begin),"\n",
		)
	}(time.Now())
	return mw.next.Health()
}
func (mw *loggingMiddleware)ApplyCoupon(couponid string)(couponStatus CouponStatus){
	defer func(begin time.Time) {
		mw.logger.Print("\n",
			"method\t", "ApplyCoupon","\n",
			"result\t", couponStatus,"\n",
			"took\t", time.Since(begin),"\n",
		)
	}(time.Now())
	return mw.next.ApplyCoupon(couponid)
}