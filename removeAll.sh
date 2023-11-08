#!/bin/bash

# Stop and remove all Docker containers
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)