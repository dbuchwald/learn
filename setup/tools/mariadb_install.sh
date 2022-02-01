#!/bin/bash
if [ $EUID -ne 0 ]
then
  echo "ERROR: This script must be executed as root"
  exit 1
fi

echo "Checking if MySQL server is installed..."
INSTALLATION_STATUS=`apt-cache policy mariadb-server | grep 'Installed:' | sed -e 's/\s*Installed:\s*//'`
echo "...done"
if [ "${INSTALLATION_STATUS}" == "(none)" ]
then
  echo "MariaDB Server is not installed, installing..."

  #installation of MySQL database server
  apt-get install -y mariadb-server libmariadb-java

  echo "...done"
  
else
  echo "MySQL Server is already installed"
fi

#enabling remote access to the server (commenting out bind-address value that limits connections to localhost only)
mariadb_configuration_file=/etc/mysql/mariadb.conf.d/50-server.cnf

if grep -q '^bind-address' ${mariadb_configuration_file}
then
  echo "Enabling remote access to MySQL Server..."
  systemctl stop mariadb.service
  mv ${mariadb_configuration_file} ${mariadb_configuration_file}.old
  sed '/^bind-address/ s/^/#/' ${mariadb_configuration_file}.old > ${mariadb_configuration_file}
  rm ${mariadb_configuration_file}.old
  systemctl start mariadb.service
  echo "...done"
else
  echo "Remote access to MySQL Server already enabled."
fi
  
mariadb --user=root <<EOT
    UPDATE mysql.user SET Password=PASSWORD('MariaDBRootPassword') WHERE User='root';
    DELETE FROM mysql.user WHERE User='';
    DROP DATABASE IF EXISTS test;
    DELETE FROM mysql.db WHERE Db='test' OR Db='test\\_%';
    FLUSH PRIVILEGES;
    DROP DATABASE IF EXISTS mydb;
    DROP USER IF EXISTS 'mariadbuser'@'%';
    FLUSH PRIVILEGES;
    CREATE DATABASE mydb;
    CREATE USER 'mariadbuser'@'%' IDENTIFIED BY 'MariaDBPassword';
    GRANT ALL PRIVILEGES ON *.* TO 'mariadbuser'@'%';
    GRANT GRANT OPTION ON *.* TO 'mariadbuser'@'%';
    FLUSH PRIVILEGES;
EOT

