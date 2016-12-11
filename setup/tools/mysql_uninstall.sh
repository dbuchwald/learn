#!/bin/bash

echo "Checking if MySQL server is installed..."
INSTALLATION_STATUS=`apt-cache policy mysql-server | grep 'Installed:' | sed -e 's/\s*Installed:\s*//'`
echo "...done"
if [ "${INSTALLATION_STATUS}" == "(none)" ]
then
  echo "MySQL Server is not installed."


  echo "...done"
  
else
  echo "MySQL Server is installed, removing..."
  apt-get remove -y mysql-server libmysql-java
  apt autoremove
  echo "...done"
fi

