#!/bin/sh

mysql -h 127.0.0.1 --user=mysqluser --password=MySQLPassword <<EOT
    DROP DATABASE IF EXISTS simplejpaapp;
    DROP USER IF EXISTS 'jpasqluser'@'%';
EOT
