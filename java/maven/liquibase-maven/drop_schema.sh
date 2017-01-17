#!/bin/sh

mysql -h 127.0.0.1 --user=mysqluser --password=MySQLPassword <<EOT
    DROP DATABASE IF EXISTS liquibase_maven;
    DROP USER IF EXISTS 'mavensqluser'@'%';
EOT
