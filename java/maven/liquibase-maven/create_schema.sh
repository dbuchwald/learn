#!/bin/sh

mysql -h 127.0.0.1 --user=mysqluser --password=MySQLPassword <<EOT
    DROP DATABASE IF EXISTS liquibase_maven;
    DROP USER IF EXISTS 'mavensqluser'@'%';
    FLUSH PRIVILEGES;
    CREATE DATABASE liquibase_maven;
    CREATE USER 'mavensqluser'@'%' IDENTIFIED BY 'MySQLPassword';
    GRANT ALL PRIVILEGES ON liquibase_maven.* TO 'mavensqluser'@'%';
    FLUSH PRIVILEGES;
EOT
