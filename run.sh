#!/bin/bash
network_name="natsukashiiz-server-shop_server-net"
container_name="server-container"
image_name="server"

# Check if a Docker container with the name "server-container" is running
if docker ps | grep -q "$container_name"; then
  echo "The "$container_name" is already running. Skipping docker-compose."
else
  # Start Docker containers using docker-compose
  docker-compose up -d

  # Wait for all background processes (including Docker containers) to finish
  wait
fi

# Build the Maven project
mvn clean install

# Check if the Maven build was successful or if there were any errors
if [ $? -eq 0 ]; then
  echo "Maven build succeeded."
  # Build a Docker image
  docker build -t $image_name .

  # Run a Docker container
  docker run --network $network_name --name $container_name -p 8080:8080 -d $image_name

  # Display a list of running Docker containers
  docker ps
else
  echo "Maven build failed."
fi
