#!/bin/sh
cd /root/sockshop/carts/
chmod -R 770 /root/sockshop/carts
cp /etc/resolv.conf /tmp
sed -i s/"^.*search.*$"/"search"/g /tmp/resolv.conf
cat /tmp/resolv.conf > /etc/resolv.conf

java -jar carts.jar
