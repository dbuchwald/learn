#!/usr/bin/env bash
./create_schema.sh
mvn liquibase:update
./drop_schema.sh

