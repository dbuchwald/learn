#!/bin/bash

echo "Checking if MySQL server is installed..."
INSTALLATION_STATUS=`apt-cache policy mysql-server | grep 'Installed:' | sed -e 's/\s*Installed:\s*//'`
echo "...done"
if [ "${INSTALLATION_STATUS}" == "(none)" ]
then
  echo "MySQL Server is not installed, installing..."

  #setup of MySQL server root user password (to suppress interactive window during installation)
  #export DEBIAN_FRONTEND=noninteractive
  #debconf-set-selections <<< "mysql-community-server mysql-community-server/data-dir select ''"
  #debconf-set-selections <<< "mysql-community-server mysql-community-server/root-pass password MySQLRootPassword"
  #debconf-set-selections <<< "mysql-community-server mysql-community-server/re-root-pass password MySQLRootPassword"

  #installation of MySQL database server
  apt-get install -y mysql-server libmysql-java

  echo "...done"
  
else
  echo "MySQL Server is already installed"
fi

#enabling remote access to the server (commenting out bind-address value that limits connections to localhost only)
mysql_configuration_file=/etc/mysql/mysql.conf.d/mysqld.cnf

if grep -q '^bind-address' ${mysql_configuration_file}
then
  echo "Enabling remote access to MySQL Server..."
  systemctl stop mysql.service
  mv ${mysql_configuration_file} ${mysql_configuration_file}.old
  sed '/^bind-address/ s/^/#/' ${mysql_configuration_file}.old > ${mysql_configuration_file}
  rm ${mysql_configuration_file}.old
  systemctl start mysql.service
  echo "...done"
else
  echo "Remote access to MySQL Server already enabled."
fi

  
mysql -u root -p MySQLRootPassword <<EOT
    DROP DATABASE IF EXISTS mydb;
    DROP USER IF EXISTS 'mysqluser'@'%';
    FLUSH PRIVILEGES;
    CREATE DATABASE mydb;
    CREATE USER 'mysqluser'@'%' IDENTIFIED BY 'MySQLPassword';
    GRANT ALL PRIVILEGES ON *.* TO 'mysqluser'@'%';
    FLUSH PRIVILEGES;
EOT

