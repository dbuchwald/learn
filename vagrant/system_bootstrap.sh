#!/usr/bin/env bash

# update system
apt-get update
apt-get upgrade -y

# environment installation
cd /environment_setup/tools
environment_install.sh

# installation of putty tools to generate PuTTy private key
apt-get install -y putty-tools

# creating putty private key for windows ssh integration
if ! [ -f /vagrant/putty_private_key ]
then
  puttygen /vagrant/.vagrant/machines/default/virtualbox/private_key -o /vagrant/putty_private_key.ppk
fi

# creating ssh script to launch PuTTy directly logging into the environment
if ! [ -f /vagrant/ssh.bat ]
then
  echo "@start /MAX putty.exe -ssh ubuntu@127.0.0.1 -P 2222 -i putty_private_key.ppk" > /vagrant/ssh.bat
fi

