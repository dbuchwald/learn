#!/bin/sh

# For the maven docker plugin to work, you have to push this image to registry
docker build -f Dockerfile.base-17-jdk -t docker-registry.local/base-17-jdk-alpine .
docker push docker-registry.local/base-17-jdk-alpine:latest