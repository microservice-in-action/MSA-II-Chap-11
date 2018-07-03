FROM mongo:3
ADD ./scripts /tmp/scripts

ENV MONGO_USER mongo-user
ENV MONGO_PASS mongo-password

# Modify child mongo to use /data/db-accounts as dbpath (because /data/db wont persist the build because it is already a VOLUME)
RUN mkdir -p /data/db-users \
    && echo "dbpath = /data/db-users" > /etc/mongodb.conf \
    && chown -R mongodb:mongodb /data/db-users
RUN echo 'db.createUser({user:"mongo-user", pwd: "mongo-password", roles: [{role: "readWrite", db: "users"}]})' > /tmp/create.js
RUN su - mongodb && mongod --fork --logpath /var/log/mongodb.log --dbpath /data/db-users  \
    && chmod +x /tmp/scripts/mongo_create_insert.sh && /tmp/scripts/mongo_create_insert.sh \
    && mongo localhost:27017/users /tmp/create.js  \
    && mongod --dbpath /data/db-users --shutdown \
    && chown -R mongodb /data/db-users

# Make the new dir a VOLUME to persist it
VOLUME /data/db-users

CMD ["mongod", "--config", "/etc/mongodb.conf", "--smallfiles"]
