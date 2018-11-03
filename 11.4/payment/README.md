# 支付服务

支付服务(Payment Service)提供的主要功能是调用第三方支付网关，完成支付。
。
支付服务使用Golang，go-chassis框架开发，对外提供REST(8088端口)和highway（9090）端口，因为实现简单，所以只考虑到对接第三方支付接口，本身没有存储状态，并未使用到数据库。

## API接口清单

 支付服务的Swagger接口定义在`api-spec`目录下。

## 开发环境准备

* 安装Golang 1.8+ : `brew install go`
* 设置`GOPATH`环境变量：
```bash
mkdir -p ~/go
export GOPATH=$HOME/go
export PATH=$PATH:$GOPATH/bin
```
* 安装`go-chassis`及相关的包: `./prepare.sh`
* 安装docker/docker-compose: `brew cask install docker-toolbox`

## 构建和打包

* 编译二进制：执行`make buildmac`，可以在`bin/`目录下生成名为`user`的二进制文件，如果是生成在容器中运行的二进制文件，执行`make build`即可。
* 打包docker镜像：`make build && docker build -t payment .`。

## 本地运行用户服务

通过docker-compose命令可以拉起注册中心、支付服务。

执行`docker-compose up -d --build`即可启动所有服务，通过以下接口确认用户服务是否正确运行并注册到注册中心：

* 是否注册到注册中心：`curl -H "X-Domain-Name:default" http://127.0.0.1:30100/registry/v3/microservices  | jq -r`

```json
   {
      "serviceId": "a981b4817ec811e8b1f50242ac170002",
      "appId": "sockshop",
      "serviceName": "payment",
      "version": "0.0.1",
      "level": "FRONT",
      "schemas": [
        "payment"
      ],
      "status": "UP",
      ...
```
* 是否有正常运行的实例：`  curl -X GET -H "X-ConsumerId: a981b4817ec811e8b1f50242ac170002" -H "X-Tenant-Name: default" http://127.0.0.1:30100/registry/v3/microservices/a981b4817ec811e8b1f50242ac170002/instances | jq -r`

```json
  "instances": [
    {
      "instanceId": "b9daa4937ece11e89bff0242ac170002",
      "serviceId": "a981b4817ec811e8b1f50242ac170002",
      "endpoints": [
        "rest://172.23.0.3:8088",
        "highway://172.23.0.3:9090"
      ],
      "hostName": "payment",
      "status": "UP",
      ...
```
> 原生的方式运行
```bash
docker-compose up -d servicecenter
make buildmac
cp -r conf/ bin/conf
export CSE_REGISTRY_ADDR=http://127.0.0.1:30100;  ./bin/payment-service -port=8088
```

* 接口测试：
    * 健康检查：`curl http://localhost:8088/health`
    * 用户列表：`curl http://localhost:8088/coupon/1`

## CI任务

空缺，流水线在ServiceStage。

## 环境访问

 * 测试环境
 * 预生产环境
 * 生产环境

## 监控

* 监控Dashboard

## FAQ

常见问题
