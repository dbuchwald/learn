#!/bin/bash
if [ $EUID -ne 0 ]
then
  echo "ERROR: This script must be executed as root"
  exit 1
fi

if ! ./wildfly_uninstall.sh
then
  echo "ERROR: WildFly application server uninstallation failed!"
  exit 1
fi

if ! ./mysql_uninstall.sh
then
  echo "ERROR: MySQL Server uninstallation failed!"
  exit 1
fi

