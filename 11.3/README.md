# 订单服务

订单服务（Order Service）提供的主要功能包括：

* 订单创建
* 订单支付
* 历史订单查询

订单支付功能依赖于支付服务，此处为了演示，当创建订单时，直接调用支付服务网关。

订单采用Java Chassis进行开发，订单信息存储在Mongodb数据库中。运行依赖于注册中心以及Mongodb数据库。

## 1. 开发环境准备

在开始使用Java Chassis 开发Catalogue服务前，需要准备开发环境，具体内容包括：

* 安装 JDK 1.8 
* 安装 Maven 3.X
* 安装 Docker（可选，方便本地调试）
* 安装 MongoDB（也可直接通过Docker镜像启动）

## 2. 构建和打包

* 生成jar包

```
    mvn clean package
```

此命令会下载项目的依赖包，并在target目录下生成orders.jar。

* 构建orders镜像（可选）

购物车服务的Dockerfile位于代码根目录下，运行以下命令即可生成docker镜像。

```
  docker build -t orders .
```

## 3. 运行订单服务

### 以docker-compose方式运行（推荐）

```
    docker-compose up
```

运行过程中能够看到，docker-compose会下载服务中心镜像servicecomb/service-center，Mongo数据库镜像mongo，并生成购服务服务镜像orders-service。

### 以原生方式运行

1. 启动MongoDB

```
docker run -d -p 27017:27017 mongo:latest
```

2. 启动服务中心

```
docker run -d -p 30100:30100 servicecomb/service-center:latest
```

3. 运行服务

```
java -jar  -MONGODB_CARTS_SERVICE_HOST=127.0.0.1 -DSC_HOST=127.0.0.1 orders.jar
```

或

```
docker run -e SC_HOST=127.0.0.1 -e MONGODB_CARTS_SERVICE_HOST=127.0.0.1 -p 7073:7073 orders
```

购物车服务运行依赖于数据库及服务中心，分别通过MONGODB_CARTS_SERVICE_HOST、DCS_HOST环境变量进行配置。

### 接口测试：

* 健康性检查接口：`curl http://localhost:7073/orders/health`
* 创建订单：`curl -l -H "Content-type: application/json" -X POST -d '{"customerId": "1", "items": [{"itemId":"a0a4f044-b040-410d-8ead-4de0446aec7e","quantity": 1, "unitPrice": 20}], "address": "测试地址"}' http://localhost:7073/orders`
* 获取用户订单列表：`curl http://localhost:7073/orders/customerId/1`
* 获取订单详情：`curl http://localhost:7073/orders/5b82d9e8e64ada000bb33f21`

此处a0a4f044-b040-410d-8ead-4de0446aec7e为测试商品id，商品id需要首先调用商品服务获取。5b82d9e8e64ada000bb33f21为订单号，创建订单时生成。

购物车服务端口为7073，可在src/main/resources/microserivce.yaml中进行修改：

```
rest:
    address: 0.0.0.0:7073
```

## 4. FAQ

* 是否注册到注册中心：`curl -H "X-Domain-Name:default" http://127.0.0.1:30100/registry/v3/microservices  | jq -r`

```json
  {
      "serviceId": "a4f5bdbca93a11e8b0f90242ac120002",
      "appId": "sockshop",
      "serviceName": "orders",
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
        "rest://172.22.0.4:7073"
      ],
      "hostName": "11b634ed4c70",
      "status": "UP",
      ...
```
