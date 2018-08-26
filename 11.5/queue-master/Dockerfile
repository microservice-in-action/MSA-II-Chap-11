FROM java:8
ENV APP_ROOT=/root/sockshop/queue-master/
ENV LOG_ROOT=/var/log/queue-master/
RUN mkdir -p $APP_ROOT
RUN mkdir -p $LOG_ROOT

COPY ./target/queue-master.jar $APP_ROOT
COPY ./queue-master.sh $APP_ROOT


RUN cd $APP_ROOT && chmod -R 770 .
RUN chmod +x /root/sockshop/queue-master/queue-master.sh

ENTRYPOINT ["/root/sockshop/queue-master/queue-master.sh"]
