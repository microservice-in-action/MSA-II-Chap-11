#!/bin/sh
cd /root/sockshop/queue-master/
chmod -R 770 /root/sockshop/queue-master
cp /etc/resolv.conf /tmp
sed -i s/"^.*search.*$"/"search"/g /tmp/resolv.conf
cat /tmp/resolv.conf > /etc/resolv.conf

java -jar queue-master.jar
