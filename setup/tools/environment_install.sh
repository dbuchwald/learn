#!/bin/bash
if [ $EUID -ne 0 ]
then
  echo "ERROR: This script must be executed as root"
  exit 1
fi

if ! ./download_tools.sh 
then
  echo "ERROR: Download script failed!"
  exit 1
fi

if ! ./mariadb_install.sh
then
  echo "ERROR: MariaDB Server installation failed!"
  exit 1
fi

#if ! ./wildfly_install.sh
#then
#  echo "ERROR: WildFly application server installation failed!"
#  exit 1
#fi

if ! ./misc_install.sh
then
  echo "ERROR: Miscellaneous tools installation failed!"
  exit 1
fi


