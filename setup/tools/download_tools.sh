#!/bin/bash
. ./packages_setup.sh

#if ! [ -d ./netbeans ]
#then
#  echo "Creating directory netbeans..."
#  mkdir -p ./netbeans
#  echo "...done"
#fi
#
#if ! [ -f ./netbeans/${NETBEANS_INSTALLER} ]
#then
#  echo "Downloading NetBeans installer..."
#  wget -O ./netbeans/${NETBEANS_INSTALLER} --quiet --tries=0 --continue http://download.netbeans.org/netbeans/${NETBEANS_VERSION}/final/bundles/${NETBEANS_INSTALLER} 
#  echo "...done"
#fi
#
#if ! [ -x ./netbeans/${NETBEANS_INSTALLER} ]
#then
#  echo "Setting NetBeans installer to be executable..."
#  chmod u+x ./netbeans/${NETBEANS_INSTALLER}
#  echo "...done"
#fi

if ! [ -d ./eclipse ]
then
  echo "Creating directory eclipse..."
  mkdir -p ./eclipse
  echo "...done"
fi

if ! [ -f ./eclipse/eclipse.tar.gz ]
then
  echo "Downloading eclipse package..."
  wget -O .eclipse/eclipse.tar.gz --quied --tries=0 --continue http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/neon/1a/eclipse-jee-neon-1a-linux-gtk-x86_64.tar.gz
  echo "...done"
fi

if ! [ -d ./wildfly ]
then
  echo "Creading directory wildfly..."
  mkdir -p ./wildfly
  echo "...done"
fi

if ! [ -f ./wildfly/${WILDFLY_PACKAGE} ]
then
  echo "Downloading WildFly package..."
  wget -O ./wildfly/${WILDFLY_PACKAGE} --quiet --tries=0 --continue http://download.jboss.org/wildfly/${WILDFLY_VERSION}.Final/${WILDFLY_PACKAGE}
  echo "...done"
fi


