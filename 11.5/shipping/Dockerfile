FROM java:8
ENV APP_ROOT=/root/sockshop/shipping/
ENV LOG_ROOT=/var/log/shipping/
RUN mkdir -p $APP_ROOT
RUN mkdir -p $LOG_ROOT

COPY ./target/shipping.jar $APP_ROOT
COPY ./shipping.sh $APP_ROOT


RUN cd $APP_ROOT && chmod -R 770 .
RUN chmod +x /root/sockshop/shipping/shipping.sh

ENTRYPOINT ["/root/sockshop/shipping/shipping.sh"]
