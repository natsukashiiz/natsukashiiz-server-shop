## application-local.yml
```yaml
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/<DATABASE NAME>?useSSL=false&serverTimezone=UTC
    username: <MYSQL_USER>
    password: <MYSQL_PASSWORD>
    
  redis:
    host: localhost
    port: 6379
```
### Example
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

## application-test.yml
```yaml
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysqldb:3306/<DATABASE NAME>?useSSL=false&serverTimezone=UTC
    username: <MYSQL_USER>
    password: <MYSQL_PASSWORD>
    
  redis:
    host: redis
    port: 6379
```
### Example
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

<hr>

## Docker compose run

```shell
docker-compose up -d
```

## Build image

```shell
docker build -t server .
```

## Docker network list
```shell
docker network ls
```
### Result
```text
NETWORK ID     NAME                                  DRIVER    SCOPE
121d79c713ea   natsukashiiz-server-shop_server-net   bridge    local
```

## Run container

```shell
docker run --network <NETWORK NAME> --name server-container -p 8080:8080 -d server
```
### Example
```shell
docker run --network natsukashiiz-server-shop_server-net --name server-container -p 8080:8080 -d server
```

## URL Application running

http://localhost:8080

## Docker container list
```shell
docker ps -a
```

### Result
```text
CONTAINER ID   IMAGE       COMMAND                  CREATED             STATUS                           PORTS     NAMES
7570d06b7592   redis       "docker-entrypoint.s…"   About an hour ago   Exited (0) About an hour ago               redis
25d2c123f2a3   mysql:5.7   "docker-entrypoint.s…"   About an hour ago   Exited (0) About an hour ago               mysqldb
f5c76f91214d   server      "java -server -jar /…"   About an hour ago   Exited (143) About an hour ago             server-container
```

## Stop container
```shell
docker stop mysqldb redis server-container
```

## Start container
```shell
docker start mysqldb redis server-container
```