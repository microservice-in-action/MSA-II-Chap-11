# 购物车服务

购物车服务(Cart Service)提供的主要功能包括：

* 将商品加入购物车。
* 获取购物车里的商品。
* 更新购物车中的商品。
* 将商品从购物车移出。

采用Java Chassis进行开发，购物车状态存储在Mongodb数据库中。运行依赖注册中心以及Mongodb数据库。

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

此命令会下载项目的依赖包，并在target目录下生成carts.jar。

* 构建carts镜像（可选）

购物车服务的Dockerfile位于代码根目录下，运行以下命令即可生成docker镜像。

```
  docker build -t carts .
```

## 3. 运行购物车服务

### 以docker-compose方式运行（推荐）

```
    docker-compose up
```

运行过程中能够看到，docker-compose会下载服务中心镜像servicecomb/service-center，Mongo数据库镜像mongo，并生成购服务服务镜像carts-service。

### 以原生方式运行

1. 启动MongoDB

```
docker run -d -p 27017:27017 mongo-db
```

2. 启动服务中心

```
docker run -d -p 30100:30100 servicecomb/service-center:latest
```

3. 运行服务

```
java -jar  -MONGODB_CARTS_SERVICE_HOST=127.0.0.1 -DSC_HOST=127.0.0.1 carts.jar
```

或

```
docker run -e SC_HOST=127.0.0.1 -e MONGODB_CARTS_SERVICE_HOST=127.0.0.1 -p 7072:7072 catalogue
```

购物车服务运行依赖于数据库及服务中心，分别通过MONGODB_CARTS_SERVICE_HOST、DCS_HOST环境变量进行配置。

### 接口测试：

* 健康性检查接口：`curl http://localhost:7072/carts/health`
* 将商品加入用户购物车：`curl -l -H "Content-type: application/json" -X POST -d '{"catalogueId":"a0a4f044-b040-410d-8ead-4de0446aec7e","quantity": 1, "unitPrice": 20}' http://localhost:7072/carts/1/items`
* 查看用户购物车：`curl http://localhost:7072/carts/1/items`

此处a0a4f044-b040-410d-8ead-4de0446aec7e为测试商品id，商品id需要首先调用商品服务获取。

购物车服务端口为7072，可在src/main/resources/microserivce.yaml中进行修改：

```
rest:
    address: 0.0.0.0:7072
```

## 4. FAQ

* 是否注册到注册中心：`curl -H "X-Domain-Name:default" http://127.0.0.1:30100/registry/v3/microservices  | jq -r`

```json
  {
      "serviceId": "a4f5bdbca93a11e8b0f90242ac120002",
      "appId": "sockshop",
      "serviceName": "carts",
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
        "rest://172.22.0.4:7072"
      ],
      "hostName": "11b634ed4c70",
      "status": "UP",
      ...
```
