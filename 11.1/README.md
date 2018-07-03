# 用户服务

用户服务(User Service)提供用户相关信息的管理。用户使用Golang，go-chassis框架开发，对外提供REST(8081端口)和highway（8080）端口，状态存储在Mongodb数据库中。用户服务的运行依赖注册中心以及Mongodb数据库。

## API接口清单

 [API接口清单](http://microservices-demo.github.io/api/index?url=https://raw.githubusercontent.com/microservices-demo/user/master/apispec/user.json)

## 开发环境准备

* 安装Golang 1.8+ : `brew install golang`
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
* 打包docker镜像：`make build && docker build -t user .`。

## 本地运行用户服务

通过docker-compose命令可以拉起注册中心、用户服务以及Mongodb数据库。数据库中会预置一些用户数据，准备的脚本在`docker/user-db/scripts`下面（测试用户脚本为`scripts/customer-insert.js`）。

执行`docker-compose up -d`即可启动所有服务，通过以下接口确认用户服务是否正确运行并注册到注册中心：

* 是否注册到注册中心：`curl -H "X-Domain-Name:default" http://127.0.0.1:30100/registry/v3/microservices  | jq -r`

```json
  {
      "serviceId": "d7d192c37e7211e8b30c0242ac160002",
      "appId": "sockshop",
      "serviceName": "user",
      "version": "0.0.1",
      "level": "FRONT",
      "status": "UP",
      ...
```
* 是否有正常运行的实例：`  curl -X GET -H "X-ConsumerId: d7d192c37e7211e8b30c0242ac160002" -H "X-Tenant-Name: default" http://127.0.0.1:30100/registry/v3/microservices/d7d192c37e7211e8b30c0242ac160002/instances | jq -r`

```json
{
  "instances": [
    {
      "instanceId": "4df052937e7511e8b30c0242ac160002",
      "serviceId": "d7d192c37e7211e8b30c0242ac160002",
      "endpoints": [
        "rest://172.22.0.4:8081",
        "highway://172.22.0.4:8080"
      ],
      "hostName": "user",
      "status": "UP",
      ...
```
> 原生的方式运行
```bash
docker-compose up -d user-db servicecenter
make buildmac
CSE_SERVICE_CENTER=http://127.0.0.1:30100 ./bin/user -port=8080 -database=mongodb -mongo-host=localhost:27017
```

* 接口测试：
    * 健康检查：`curl http://localhost:8080/health`
    * 用户列表：`curl http://localhost:8080/customers`

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
