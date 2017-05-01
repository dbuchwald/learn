#!/bin/bash

./create_schema.sh

mvn liquibase:update clean package

mvn exec:java -Dexec.mainClass="net.dbuchwald.learn.mybatis.SimpleMyBatisApp" -Dexec.cleanupDaemonThreads="false"

./drop_schema.sh
