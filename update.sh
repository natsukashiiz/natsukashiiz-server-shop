#!/bin/bash
mvn clean install
docker stop server-container
docker rm server-container
docker build -t server .
docker run --network natsukashiiz-server-shop_server-net --name server-container -p 8080:8080 -d server
docker ps