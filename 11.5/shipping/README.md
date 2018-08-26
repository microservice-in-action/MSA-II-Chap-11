# 物流服务

物流服务（Shipping Service）提供物流相关的功能，并与第三方物流系统进行集成。其主要包括以下功能：

* 物流创建
* 物流信息查询
* 所有物流信息获取

订单采用Java Chassis进行开发，运行依赖于注册中心、RabbitMQ。

对于物流信息的处理，将它放在queue-master中进行异步处理，当物流服务创建物流后，将它放入消息对象RabbitMQ，由queue-master获取物流信息并进行处理。

## 1. 开发环境准备

在开始使用Java Chassis 开发Catalogue服务前，需要准备开发环境，具体内容包括：

* 安装 JDK 1.8
* 安装 Maven 3.X
* 安装 Docker（可选，方便本地调试）
* 安装 RabbitMQ（也可直接通过Docker镜像启动）

## 2. 构建和打包

* 生成jar包

```
    mvn clean package
```

此命令会下载项目的依赖包，并在target目录下生成shipping.jar。

* 构建orders镜像（可选）

购物车服务的Dockerfile位于代码根目录下，运行以下命令即可生成docker镜像。

```
  docker build -t shipping .
```

## 3. 运行物流服务&物流网关（queue-master）

### 以docker-compose方式运行（推荐）

```
    docker-compose up
```

运行过程中能够看到，docker-compose会下载服务中心镜像servicecomb/service-center，Mongo数据库镜像rabbitmq:3，并生成物流网关服务镜像queue-master，物流服务镜像shipping。

```
Creating shipping_queue-master_1     ... done
Creating shipping_rabbitmq_1         ... done
Creating shipping_shipping-service_1 ... done
Creating shipping_servicecenter_1    ... done
```

### 以原生方式运行

1. 启动RabbitMQ

```
docker run -d -p 5671:5671 rabbitmq:3
```

2. 启动服务中心

```
docker run -d -p 30100:30100 servicecomb/service-center:latest
```

3. 运行服务

```
java -jar  -RABBITMQ_SERVICE_HOST=127.0.0.1 -DSC_HOST=127.0.0.1 shipping.jar
```

或

```
docker run -e SC_HOST=127.0.0.1 -e RABBITMQ_SERVICE_HOST=127.0.0.1 -p 7074:7074 shipping
```

4. 运行物流网关服务（queue-master）

```
docker run -d  --name queue-master -p 7075:7075 -e RABBITMQ_SERVICE_HOST=127.0.0.1 -e SC_HOST=127.0.0.1 queue-master
```

物流服务及物流网关运行依赖于RabbitMQ及服务中心，通过RABBITMQ_SERVICE_HOST、DCS_HOST环境变量进行配置。

### 接口测试：

* 健康性检查接口：`curl http://localhost:7074/shipping/health`
* 创建物流：`curl -l -H "Content-type: application/json" -X POST -d '{"shipment": {"name":"Country_Province_City_STREET_NO1","amount": 3}}' http://localhost:7074/shipping/userAcurl -l -H "Content-type: application/json" -X POST -d '{"shipment": {"name":"Country_Province_City_STREET_NO1","amount": 3}}' http://localhost:7074/shipping/userA`

此时在shipping服务及queue-master服务的日志中分别能看到：

```
shipping-service_1  | Adding shipment to queue...userA
queue-master_1      | Received shipment task:
```

* 查询物流信息：`curl http://localhost:7074/shipping/dec3f235-b524-48e7-a281-5d21901a0b2b`

此处dec3f235-b524-48e7-a281-5d21901a0b2b为物流id，创建物流时生成。

物流服务端口为7074，可在src/main/resources/microserivce.yaml中进行修改：

```
rest:
    address: 0.0.0.0:7074
```

## 4. FAQ

* 是否注册到注册中心：`curl -H "X-Domain-Name:default" http://127.0.0.1:30100/registry/v3/microservices  | jq -r`

```json
  {
      "serviceId": "a4f5bdbca93a11e8b0f90242ac120002",
      "appId": "sockshop",
      "serviceName": "shipping",
      "version": "0.0.1",
      "level": "FRONT",
      "status": "UP",
      ...
```
* 是否有正常运行的实例：`  curl -X GET -H "X-ConsumerId: a4f5bdbca93a11e8b0f90242ac120002" -H "X-Tenant-Name: default" http://127.0.0.1:30100/registry/v3/microservices/a4f5bdbca93a11e8b0f90242ac120002/instances | jq -r`

```json
{
  "instances": [
    {
      "instanceId": "e612bcbaa94011e88a170242ac120002",
      "serviceId": "a4f5bdbca93a11e8b0f90242ac120002",
      "endpoints": [
        "rest://172.22.0.4:7074"
      ],
      "hostName": "11b634ed4c70",
      "status": "UP",
      ...
```
