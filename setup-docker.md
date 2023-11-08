# applicaiotn-local.yml
```yaml
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/shop_db?useSSL=false&serverTimezone=UTC
    username: dev
    password: 1234

  redis:
    host: localhost
    port: 6379
```

# applicaiotn-test.yml
```yaml
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysqldb:3306/shop_db?useSSL=false&serverTimezone=UTC
    username: dev
    password: 1234

  redis:
    host: redis
    port: 6379
```

## Docker compose run

```shell
docker-compose up -d
```

## Build image

```shell
docker build -t server .
```

## Run container

```shell
docker run --network natsukashiiz-server-shop_server-net --name server-container -p 8080:8080 -d server
```

## URL Application running

http://localhost:8080

## Stop container
```shell
docker stop server-container
```