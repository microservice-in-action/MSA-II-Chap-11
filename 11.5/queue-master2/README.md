# 物流网关

当物流服务将物流信息放入消息队列（rabbitmq）时，物流网关（Queue master）负责从消息队列中读取物流信息，进行处理。

物流网关基于Java Chassis开发，依赖于注册中心及RabbitMQ。

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

此命令会下载项目的依赖包，并在target目录下生成queue-master.jar。

* 构建queue-master镜像（可选）

购物车服务的Dockerfile位于代码根目录下，运行以下命令即可生成docker镜像。

```
  docker build -t queue-master .
```

## 3. 运行物流网关

### 通过Docker镜像启动（推荐）

```
    docker run -d  --name queue-master -p 7075:7075 -e RABBITMQ_SERVICE_HOST=rabbitmq -e SC_HOST=servicecenter queue-master
```

物流网关服务运行依赖于RabbitMQ及服务中心，RABBITMQ_SERVICE_HOST、DCS_HOST环境变量进行配置。

### 接口测试：

* 健康性检查接口：`curl http://localhost:7075/queue_master/health`

购物车服务端口为7075，可在src/main/resources/microserivce.yaml中进行修改：

```
rest:
    address: 0.0.0.0:7075
```

* 物流处理任务

由于物流处理为异步任务ShippingTaskHandler，当新的物流信息被物流服务放入RabbitMQ时，能够看到处理日志：

```
Received shipment task:
```

## 4. FAQ

* 是否注册到注册中心：`curl -H "X-Domain-Name:default" http://127.0.0.1:30100/registry/v3/microservices  | jq -r`

```json
  {
      "serviceId": "a4f5bdbca93a11e8b0f90242ac120002",
      "appId": "sockshop",
      "serviceName": "queuemaster",
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
        "rest://172.22.0.4:7075"
      ],
      "hostName": "11b634ed4c70",
      "status": "UP",
      ...
```
