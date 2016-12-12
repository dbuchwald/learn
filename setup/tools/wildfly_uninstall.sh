#!/bin/bash
if [ $EUID -ne 0 ]
then
  echo "ERROR: This script must be executed as root"
  exit 1
fi

. ./packages_setup.sh

if ! [ -d ${WILDFLY_INSTALLATION_FOLDER} ]
then
  echo "WildFly application server is not installed."
else
  echo "WildFly application server is installed, removing..."
  systemctl stop wildfly.service
  rm -rf ${WILDFLY_INSTALLATION_FOLDER}
  rm -f /etc/init.d/wildfly
  rm -f /etc/default/wildfly
  update-rc.d wildfly remove

  echo "...done"
fi

if id wildfly
then
  echo "Deleting user wildfly..."
  userdel wildfly
  echo "...done"
else
  echo "No need to delete user wildfly, already deleted."
fi

