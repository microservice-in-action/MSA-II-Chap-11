#!/bin/sh
cd /root/sockshop/orders/
chmod -R 770 /root/sockshop/orders
cp /etc/resolv.conf /tmp
sed -i s/"^.*search.*$"/"search"/g /tmp/resolv.conf
cat /tmp/resolv.conf > /etc/resolv.conf

java -jar orders.jar
