FROM java:8
ENV APP_ROOT=/root/sockshop/orders/
ENV LOG_ROOT=/var/log/orders/
RUN mkdir -p $APP_ROOT
RUN mkdir -p $LOG_ROOT

COPY ./target/orders.jar $APP_ROOT
COPY ./orders.sh $APP_ROOT


RUN cd $APP_ROOT && chmod -R 770 .
RUN chmod +x /root/sockshop/orders/orders.sh

ENTRYPOINT ["/root/sockshop/orders/orders.sh"]
