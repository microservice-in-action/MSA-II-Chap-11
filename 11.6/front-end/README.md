[![Build Status](https://travis-ci.org/microservices-demo/front-end.svg?branch=master)](https://travis-ci.org/microservices-demo/front-end)
[![](https://images.microbadger.com/badges/image/weaveworksdemos/front-end.svg)](http://microbadger.com/images/weaveworksdemos/front-end "Get your own image badge on microbadger.com")


Front-end app
---
Front-end application written in [Node.js](https://nodejs.org/en/) that puts together all of the microservices under [microservices-demo](https://github.com/microservices-demo/microservices-demo).

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
  </tbody>
</table>

example: docker-image-name:version-tag --> sockshop-frontend-service:0.7.0-SNAPSHOT

## Step1: 
git clone git@github.com:huawei-microservice-demo/front-end.git
## Step2:
cd front-end

## Step3: Build the docker image
docker build --no-cache=true -t [docker-image-name:version-tag] .

## Step4: Tag the image with service stage
docker tag [docker-image-name:version-tag]  registry.cn-north-1.hwclouds.com/[service-stage-username]/[docker-image-name:version-tag]

## Step5: Docker login
docker login -u [username] -p [private-key] [registry-name]

## Step6: Docker push
docker push registry.cn-north-1.hwclouds.com/[service-stage-username]/[docker-image-name:version-tag]

## Step7: 
Login to the service and get the ip address / domain to open microservice in browser