#!/bin/bash
docker-compose up -d
sleep 5
mvn clean install
docker build -t server .
docker stop server-container
docker rm server-container
docker run --network natsukashiiz-server-shop_server-net --name server-container -p 8080:8080 -d server
docker ps