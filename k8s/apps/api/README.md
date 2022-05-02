# Sample API application for k8s exercises

This repo contains all the object necessary for experimenting with
application lifecycle management inside Kubernetes cluster. The point
is to be able to start the application with simple data store, test
API evolution approach (backed up by Liquibase migrations), experiment
with Spring-based integration and unit testing.

## Development and testing

Some commands that might be useful for local development and testing:

Building the application

`mvn clean package`

Running integration tests

`mvn clean verify`

Running application locally as a Spring Boot application

`mvn spring-boot:run`

Running with custom database provider (h2/derby/mariadb)

`mvn -Dspring-boot.run.profiles=mariadb spring-boot:run`

Running as a Docker container (after it's built)

`docker run --rm --name api --publish 8080:8080 --env SPRING_PROFILES_ACTIVE=derby dawidbuchwald/k8s-learn-api-v1`

Running both application and MariaDB in containers

`docker network create --driver bridge api-net`

`docker run --rm --name mariadb_api --detach --network api-net --env MARIADB_USER=api --env MARIADB_PASSWORD=mypass --env MARIADB_RANDOM_ROOT_PASSWORD=1 --env MARIADB_DATABASE=api docker.io/library/mariadb:10.7`

`docker run --rm --name api --detach --publish 8080:8080 --network api-net --env SPRING_PROFILES_ACTIVE=mariadb --env API_DATABASE_HOSTNAME=mariadb_api dawidbuchwald/k8s-learn-api-v1`

`docker stop api`

`docker stop mariadb_api`

`docker network remove api-net`

Running with Docker Compose:

`docker-compose up -d`

`docker-compose down`