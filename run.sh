#!/bin/bash

# Check if a Docker container with the name "server-container" is running
if docker ps | grep -q 'server-container'; then
  echo "The 'server-container' is already running. Skipping docker-compose."
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
  docker build -t server .

  # Run a Docker container
  docker run --network natsukashiiz-server-shop_server-net --name server-container -p 8080:8080 -d server

  # Display a list of running Docker containers
  docker ps
else
  echo "Maven build failed."
fi
