#!/bin/bash
. ./packages_setup.sh

if [ -f ./netbeans/${NETBEANS_INSTALLER} ]
then
  rm -f ./netbeans/${NETBEANS_INSTALLER}
fi

if [ -d ./netbeans ]
then
  rmdir ./netbeans
fi

if [ -f ./eclipse/eclipse.tar.gz ]
then
  rm -f ./eclipse/eclipse.tar.gz 
fi

if [ -d ./eclipse ]
then
  rmdir ./eclipse
fi

if [ -f ./wildfly/${WILDFLY_PACKAGE} ]
then
  rm -f ./wildfly/${WILDFLY_PACKAGE}
fi

if [ -d ./wildfly ]
then
  rmdir ./wildfly
fi


