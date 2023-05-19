#!/bin/bash

docker run --env "ACCEPT_EULA=Y" \
           --env "MSSQL_SA_PASSWORD=MSSQL_Strong_P@ssw0rd" \
           --publish 1433:1433 \
           --name mssql_2019 \
           --hostname mssql_2019 \
           --detach \
           --rm \
           mcr.microsoft.com/mssql/server:2019-latest

docker cp create_database.sql mssql_2019:/tmp/create_database.sql

docker exec mssql_2019 /opt/mssql-tools/bin/sqlcmd \
            -S localhost \
            -U SA \
            -P "MSSQL_Strong_P@ssw0rd" \
            -i /tmp/create_database.sql
