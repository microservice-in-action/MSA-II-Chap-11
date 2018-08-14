FROM java:8
ENV APP_ROOT=/root/sockshop/carts/
ENV LOG_ROOT=/var/log/carts/
RUN mkdir -p $APP_ROOT
RUN mkdir -p $LOG_ROOT

COPY ./target/carts.jar $APP_ROOT
COPY ./carts.sh $APP_ROOT


RUN cd $APP_ROOT && chmod -R 770 .
RUN chmod +x /root/sockshop/carts/carts.sh

ENTRYPOINT ["/root/sockshop/carts/carts.sh"]
