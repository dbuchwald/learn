#!/bin/sh

mysql --host=localhost --protocol=tcp --user=mysqluser --password=MySQLPassword <<EOT
    DROP DATABASE IF EXISTS simplejpaapp;
    DROP USER IF EXISTS 'jpasqluser'@'%';
    FLUSH PRIVILEGES;
    CREATE DATABASE simplejpaapp;
    CREATE USER 'jpasqluser'@'%' IDENTIFIED BY 'MySQLPassword';
    GRANT ALL PRIVILEGES ON simplejpaapp.* TO 'jpasqluser'@'%';
    FLUSH PRIVILEGES;
EOT
