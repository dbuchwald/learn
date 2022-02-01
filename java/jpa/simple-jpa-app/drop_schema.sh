#!/bin/sh

mariadb --host=localhost --protocol=tcp --user=mariadbuser --password=MariaDBPassword <<EOT
    DROP DATABASE IF EXISTS simplejpaapp;
    DROP USER IF EXISTS 'jpasqluser'@'%';
EOT
