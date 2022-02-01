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
  echo "MySQL Server is not installed."
else
  echo "MySQL Server is installed, removing..."
  apt-get remove --purge -y mariadb-server mariadb-client mariadb-common
  apt autoremove -y
  apt autoclean -y
  rm -rf /var/lib/mariadb
  rm -rf /etc/mariadb
  echo "...done"
fi

