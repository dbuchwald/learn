#!/bin/bash
if [ $EUID -ne 0 ]
then
  echo "ERROR: This script must be executed as root"
  exit 1
fi

. ./packages_setup.sh

echo "Installing OpenJDK"
apt-get install -y default-jdk

if ! id wildfly
then
  echo "Creating user wildfly..."
  adduser --system --group --no-create-home --disabled-password --disabled-login --gecos "" wildfly
  echo "...done"
else
  echo "No need to create user wildfly, already created."
fi

if ! [ -d ${WILDFLY_INSTALLATION_FOLDER}  ]
then
  if ! [ -f ./wildfly/${WILDFLY_PACKAGE} ]
  then
    echo "ERROR: WildFly package not downloaded yet, run download_tools.sh first!"
    exit 1
  fi
  echo "WildFly application server is not installed, installing..."

  mkdir -p ${WILDFLY_INSTALLATION_FOLDER}
  echo "...folder created..."
  cp ./wildfly/${WILDFLY_PACKAGE} ${WILDFLY_INSTALLATION_FOLDER}
  echo "...package copied..."
  cd ${WILDFLY_INSTALLATION_FOLDER}
  tar -xzf ${WILDFLY_PACKAGE}
  echo "...package extracted..."
  WILDFLY_PACKAGE_FOLDER=`basename ${WILDFLY_PACKAGE} .tar.gz`
  ln -s ${WILDFLY_PACKAGE_FOLDER} wildfly
  echo "...symlink created..."
  chown -R wildfly.wildfly wildfly*
  echo "...ownership changed..."

  if ! [ -f /etc/init.d/wildfly ]
  then
    cp ${WILDFLY_INSTALLATION_FOLDER}/wildfly/docs/contrib/scripts/init.d/wildfly-init-debian.sh /etc/init.d/wildfly
    update-rc.d wildfly defaults
  fi
  echo "...created startup scripts..."

  #installing WildFly configuration
  if ! [ -f /etc/default/wildfly ]
  then
    cp ${WILDFLY_INSTALLATION_FOLDER}/wildfly/docs/contrib/scripts/init.d/wildfly.conf /etc/default/wildfly
    
    echo "JBOSS_HOME=\"${WILDFLY_INSTALLATION_FOLDER}/wildfly\""   >> /etc/default/wildfly
    echo "JBOSS_USER=wildfly"                                      >> /etc/default/wildfly
    echo "JBOSS_MORE=standalone"                                   >> /etc/default/wildfly
    echo "JBOSS_CONFIG=standalone-full.xml"                        >> /etc/default/wildfly
    echo "STARTUP_WAIT=120"                                        >> /etc/default/wildfly
    echo "SHUTDOWN_WAIT=120"                                       >> /etc/default/wildfly
    echo "JBOSS_CONSOLE_LOG=\"/var/log/wildfly/console.log\""      >> /etc/default/wildfly
    echo "JBOSS_OPTS=\"-Djboss.bind.address.management=0.0.0.0\""  >> /etc/default/wildfly

    echo "...defaults created..."
    
    systemctl start wildfly.service
    ${WILDFLY_INSTALLATION_FOLDER}/wildfly/bin/add-user.sh wildfly wildfly
  fi

  apt-get install -y libmysql-java
  MYSQL_CONNECTOR_JAR=`readlink /usr/share/java/mysql-connector-java.jar`

  if ! [ -f ${WILDFLY_INSTALLATION_FOLDER}/wildfly/modules/system/layers/base/com/mysql/driver/main/${MYSQL_CONNECTOR_JAR} ]
  then
    ${WILDFLY_INSTALLATION_FOLDER}/wildfly/bin/jboss-cli.sh -c -u=wildfly -p=wildfly \
	  --command="module add --name=com.mysql.driver \
	                        --resources=/usr/share/java/${MYSQL_CONNECTOR_JAR} \
							--dependencies=javax.api,javax.transaction.api "
  fi
  echo "...MySQL driver module installed..."

  ${WILDFLY_INSTALLATION_FOLDER}/wildfly/bin/jboss-cli.sh -c -u=wildfly -p=wildfly \
    --command="/subsystem=datasources/jdbc-driver=MySQLDriver:add(driver-name=MySQLDriver,\
                                                                  driver-module-name=com.mysql.driver,\
                                                                  driver-class-name=com.mysql.jdbc.Driver,\
                                                                  driver-xa-datasource-class-name=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource) "

  echo "...MySQL driver set up..."

  ${WILDFLY_INSTALLATION_FOLDER}/wildfly/bin/jboss-cli.sh -c -u=wildfly -p=wildfly \
    --command="xa-data-source add --name=MySQLXADS \
                                  --jndi-name=\"java:/jdbc/MySQLXADS1\" \
                                  --driver-name=MySQLDriver \
                                  --xa-datasource-class=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource \
                                  --user-name=mysqluser \
                                  --password=MySQLPassword \
                                  --xa-datasource-properties=[{ServerName=localhost,DatabaseName=mydb}] "

  echo "...MySQL DataSource created..."

  systemctl restart wildfly.service

  echo "...done"
  
else
  echo "WildFly application server is already installed"
fi
