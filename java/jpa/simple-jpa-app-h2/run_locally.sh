#!/bin/bash

./create_schema.sh

mvn liquibase:update clean package

mvn exec:java -Dexec.mainClass="net.dbuchwald.learn.jpa.SimpleJpaApp" -Dexec.cleanupDaemonThreads="false"

./drop_schema.sh
