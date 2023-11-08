#!/bin/bash
container_name="server-container"

if docker ps | grep -q "$container_name"; then
  docker stop $container_name
  docker rm $container_name
fi