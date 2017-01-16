#!/bin/sh

mysql --user=root --password=MySQLRootPassword <<EOT
    DROP DATABASE IF EXISTS liquibase_maven;
    DROP USER IF EXISTS 'mavensqluser'@'%';
EOT
