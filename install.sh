#!/bin/bash
docker-compose up -d
#sleep 3
wait
mvn clean install -DskipTests
docker build -t server .
docker run --network natsukashiiz-server-shop_server-net --name server-container -p 8080:8080 -d server
docker ps