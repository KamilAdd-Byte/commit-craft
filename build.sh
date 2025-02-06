#!/bin/bash

set -e

echo "Building commmit-craft project..."
./gradlew clean build

echo "Building the Docker image..."
docker build -t commmit-craft .

echo "Running the Docker container..."
docker run -d -p 8090:8090 --name commmit-craft commmit-craft
