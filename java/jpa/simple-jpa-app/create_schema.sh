#!/bin/sh

mariadb --host=localhost --protocol=tcp --user=mariadbuser --password=MariaDBPassword <<EOT
    DROP DATABASE IF EXISTS simplejpaapp;
    DROP USER IF EXISTS 'jpasqluser'@'%';
    FLUSH PRIVILEGES;
    CREATE DATABASE simplejpaapp;
    CREATE USER 'jpasqluser'@'%' IDENTIFIED BY 'MariaDBPassword';
    GRANT ALL PRIVILEGES ON simplejpaapp.* TO 'jpasqluser'@'%';
    FLUSH PRIVILEGES;
EOT
