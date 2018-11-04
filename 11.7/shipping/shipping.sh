#!/bin/sh
cd /root/sockshop/shipping/
chmod -R 770 /root/sockshop/shipping
cp /etc/resolv.conf /tmp
sed -i s/"^.*search.*$"/"search"/g /tmp/resolv.conf
cat /tmp/resolv.conf > /etc/resolv.conf

java -jar shipping.jar
