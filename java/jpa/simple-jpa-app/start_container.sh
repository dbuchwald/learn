#!/bin/sh

docker run --detach --rm --name db_server -p 3306:3306 --env MYSQL_ROOT_PASSWORD=MySQLRootPassword  mysql:latest

printf "Waiting for container to start..."
while ! mysql --user=root --password=MySQLRootPassword --protocol=tcp --host=localhost --no-beep --execute=exit 2>/dev/null; do :; printf "."; sleep 1; done
printf "done\n"

mysql --user=root --password=MySQLRootPassword --protocol=tcp --host=localhost <<EOT
    DROP DATABASE IF EXISTS mydb;
    DROP USER IF EXISTS 'mysqluser'@'%';
    FLUSH PRIVILEGES;
    CREATE DATABASE mydb;
    CREATE USER 'mysqluser'@'%' IDENTIFIED BY 'MySQLPassword';
    GRANT ALL PRIVILEGES ON *.* TO 'mysqluser'@'%';
    GRANT GRANT OPTION ON *.* TO 'mysqluser'@'%';
    FLUSH PRIVILEGES;
EOT
