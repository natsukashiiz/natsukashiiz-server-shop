#!/bin/bash

# Start Docker containers
docker-compose up -d

# Wait for Docker containers to start (optional)
# sleep 3

# Wait for all background processes (including Docker containers) to finish
wait

# Build the Maven project
mvn clean install

# Check if the Maven build was successful or if there were any errors
if [ $? -eq 0 ]; then
  # Build a Docker image
  docker build -t server .

  # Run a Docker container
  docker run --network natsukashiiz-server-shop_server-net --name server-container -p 8080:8080 -d server

  # Display a list of running Docker containers
  docker ps
else
  echo "Maven build failed."
fi