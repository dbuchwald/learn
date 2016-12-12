#!/bin/bash
if [ $EUID -ne 0 ]
then
  echo "ERROR: This script must be executed as root"
  exit 1
fi

echo "Checking if MySQL server is installed..."
INSTALLATION_STATUS=`apt-cache policy mysql-server | grep 'Installed:' | sed -e 's/\s*Installed:\s*//'`
echo "...done"
if [ "${INSTALLATION_STATUS}" == "(none)" ]
then
  echo "MySQL Server is not installed."
else
  echo "MySQL Server is installed, removing..."
  apt-get remove --purge -y mysql-server mysql-client mysql-common
  apt autoremove -y
  apt autoclean -y
  rm -rf /var/lib/mysql
  rm -rf /etc/mysql
  echo "...done"
fi

