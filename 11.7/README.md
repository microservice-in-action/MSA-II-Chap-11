## 启动pact broker

执行命令 `docker-compose -f pact-compose.yml up -d`。

在浏览器打开`http://localhost:8888`即可。

## 生成契约

## 发布契约

1. 使用pact brokder作为契约文件代理。

```
	mvn pact:publish
``` 

2. 或者将契约文件拷贝至shipping服务可访问的目录下。

## 验证契约

### 启动shipper服务

进入shipping服务目录，运行：

```
	docker-compose up
```

### 验证契约文件

```
	mvn pact:verify
```