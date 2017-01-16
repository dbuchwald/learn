#!/bin/sh

mysql --user=root --password=MySQLRootPassword <<EOT
    DROP DATABASE IF EXISTS liquibase_maven;
    DROP USER IF EXISTS 'mavensqluser'@'%';
    FLUSH PRIVILEGES;
    CREATE DATABASE liquibase_maven;
    CREATE USER 'mavensqluser'@'%' IDENTIFIED BY 'MySQLPassword';
    GRANT ALL PRIVILEGES ON *.* TO 'mavensqluser'@'%';
    FLUSH PRIVILEGES;
EOT
