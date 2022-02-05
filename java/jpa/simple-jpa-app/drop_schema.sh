#!/bin/sh

mysql --host=localhost --protocol=tcp --user=mysqluser --password=MySQLPassword <<EOT
    DROP DATABASE IF EXISTS simplejpaapp;
    DROP USER IF EXISTS 'jpasqluser'@'%';
EOT
