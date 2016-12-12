#!/bin/bash

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

