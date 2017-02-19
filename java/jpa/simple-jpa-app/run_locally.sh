#!/bin/bash

HIBERNATE_REQUIRED_LIBS_DIR=~/Documents/Development/libs

./create_schema.sh

mvn liquibase:update clean package

java -cp ${HIBERNATE_REQUIRED_LIBS_DIR}/*:target/simple-jpa-app-1.0-SNAPSHOT.jar net.dbuchwald.learn.jpa.SimpleJpaApp

./drop_schema.sh
