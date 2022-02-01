#!/bin/sh

docker run --detach --rm --name db_server -p 3306:3306 --env MARIADB_ROOT_PASSWORD=MariaDBRootPassword  mariadb:latest

sleep 10

mariadb --user=root --password=MariaDBRootPassword --protocol=tcp --host=localhost <<EOT
    DROP DATABASE IF EXISTS mydb;
    DROP USER IF EXISTS 'mariadbuser'@'%';
    FLUSH PRIVILEGES;
    CREATE DATABASE mydb;
    CREATE USER 'mariadbuser'@'%' IDENTIFIED BY 'MariaDBPassword';
    GRANT ALL PRIVILEGES ON *.* TO 'mariadbuser'@'%';
    GRANT GRANT OPTION ON *.* TO 'mariadbuser'@'%';
    FLUSH PRIVILEGES;
EOT
