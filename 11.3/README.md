# queue-master

[![Build Status](https://travis-ci.org/microservices-demo/shipping.svg?branch=master)](https://travis-ci.org/microservices-demo/orders)
[![](https://images.microbadger.com/badges/image/weaveworksdemos/orders.svg)](http://microbadger.com/images/weaveworksdemos/orders "Get your own image badge on microbadger.com")


orders app
---
orders application written in [java] that processes the orders

# Build

## Dependencies

<table>
  <thead>
    <tr>
      <th>Name</th>
      <th>Version</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><a href="https://docker.com">Docker</a></td>
      <td>= 1.11.2</td>
    </tr>
    <tr>
      <td><a href="https://maven.com">Maven</a></td>
      <td>= 3.5.0</td>
    </tr>
    <tr>
      <td><a href="https://oracle.com">JDK</a></td>
      <td>= 8</td>
    </tr>
  </tbody>
</table>

example: docker-image-name:version-tag --> sockshop-orders-service:0.0.1-SNAPSHOT

## Step1: 
git clone git@github.com:huawei-microservice-demo/orders.git

## Step2:
cd orders

## Step3: Build the JAR file with using maven
maven clean install

## Step4: Build the docker image with docker file and JAR file
docker build -t [docker-image-name:version-tag] .
put the JAR file and docker file in same folder and execute the above command.

## Step5: Tag the image with service stage
docker tag [docker-image-name:version-tag]  registry.cn-north-1.hwclouds.com/hwcse/[docker-image-name:version-tag]
Note: hwcse is service stage username.
      registry.cn-north-1.hwclouds.com is service stage available zone.
      
## Step6: Docker login
docker login -u [username] -p [private-key] [registry-name]

## Step7: Docker push
docker push registry.cn-north-1.hwclouds.com/hwcse/[docker-image-name:version-tag]

## Step8: 
Create the appliation in service stage and give the respective docker image path.

## Step9: 
Login to the service stage and check the status of application.
