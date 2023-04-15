#!/bin/sh

CERTIFICATES_DIR=certs
ROOT_CA_KEYSTORE=${CERTIFICATES_DIR}/root_ca.p12
ROOT_CA_PEM=${CERTIFICATES_DIR}/root_ca.pem
SERVER_IDENTITY_KEYSTORE=${CERTIFICATES_DIR}/server-identity.p12
SERVER_TRUST_STORE=${CERTIFICATES_DIR}/server-truststore.p12
CLIENT_IDENTITY_KEYSTORE=${CERTIFICATES_DIR}/client-identity.p12
CLIENT_TRUST_STORE=${CERTIFICATES_DIR}/client-truststore.p12
CLIENT_SIGNED_CERT=${CERTIFICATES_DIR}/client-signed-cert.cert_resp

rm -rf ${CERTIFICATES_DIR}

mkdir -p ${CERTIFICATES_DIR}

# Generate Root CA self-signed certificate (note -ext BC:C)

keytool -genkeypair -keyalg RSA -keysize 4096 \
        -storeType PKCS12 -alias root_ca -ext BC:C \
        -keystore ${ROOT_CA_KEYSTORE} -storepass changeit \
        -dname "cn=Root Certificate Authority, ou=CA, o=dbuchwald.net, c=PL"

# Generate server self-signed certificate

keytool -genkeypair -keyalg RSA -keysize 4096 \
        -storeType PKCS12 -alias server \
        -keystore ${SERVER_IDENTITY_KEYSTORE} -storepass changeit \
        -dname "cn=localhost, ou=SSL, o=dbuchwald.net, c=PL"

# Import Root CA certificate into server identity keystore for chain trust

keytool -export -alias root_ca \
        -keystore ${ROOT_CA_KEYSTORE} -storepass changeit | \
keytool -import -alias root_ca \
        -keystore ${SERVER_IDENTITY_KEYSTORE} -storepass changeit \
        -noprompt -trustcacerts

# Sign server certificate with Root CA certificate

keytool -certreq -keyalg RSA -keysize 4096 -alias server \
        -keystore ${SERVER_IDENTITY_KEYSTORE} -storepass changeit | \
keytool -gencert -keyalg RSA -keysize 4096 -alias root_ca \
        -keystore ${ROOT_CA_KEYSTORE} -storepass changeit | \
keytool -importcert -keyalg RSA -keysize 4096 -alias server \
        -keystore ${SERVER_IDENTITY_KEYSTORE} -storepass changeit \
        -noprompt -trustcacerts

# Delete temporarily imported Root CA certificate from server identity keystore

keytool -delete -alias root_ca \
        -keystore ${SERVER_IDENTITY_KEYSTORE} -storepass changeit

# Export Root CA to client truststore

keytool -exportcert -alias root_ca \
        -keystore ${ROOT_CA_KEYSTORE} -storepass changeit | \
keytool -import -alias root_ca \
        -keystore ${CLIENT_TRUST_STORE} -storepass changeit \
        -noprompt -trustcacerts

# Export Root CA in PEM file format for cURL usage

keytool -exportcert -alias root_ca -rfc -file ${ROOT_CA_PEM} \
        -keystore ${ROOT_CA_KEYSTORE} -storepass changeit

# Generate Client self-signed certificate

keytool -genkeypair -keyalg RSA -keysize 4096 \
        -storeType PKCS12 -alias client \
        -keystore ${CLIENT_IDENTITY_KEYSTORE} -storepass changeit \
        -dname "cn=client, ou=SSL, o=dbuchwald.net, c=PL"

# Import Root CA certificate into client identity keystore for chain trust

keytool -export -alias root_ca \
        -keystore ${ROOT_CA_KEYSTORE} -storepass changeit | \
keytool -import -alias root_ca \
        -keystore ${CLIENT_IDENTITY_KEYSTORE} -storepass changeit \
        -noprompt -trustcacerts

# Sign client certificate with Root CA certificate

keytool -certreq -keyalg RSA -keysize 4096 -alias client \
        -keystore ${CLIENT_IDENTITY_KEYSTORE} -storepass changeit | \
keytool -gencert -keyalg RSA -keysize 4096 -alias root_ca \
        -keystore ${ROOT_CA_KEYSTORE} -storepass changeit \
        -outfile ${CLIENT_SIGNED_CERT}

# Import signed client certificate for Java app

keytool -importcert -keyalg RSA -keysize 4096 -alias client \
        -keystore ${CLIENT_IDENTITY_KEYSTORE} -storepass changeit \
        -noprompt -trustcacerts -file ${CLIENT_SIGNED_CERT}

# Import client public key as a separate entry for cURL usage

keytool -importcert -keyalg RSA -keysize 4096 -alias client-public \
        -keystore ${CLIENT_IDENTITY_KEYSTORE} -storepass changeit \
        -noprompt -trustcacerts -file ${CLIENT_SIGNED_CERT}

# Delete temporarily imported Root CA certificate from server identity keystore

keytool -delete -alias root_ca \
        -keystore ${CLIENT_IDENTITY_KEYSTORE} -storepass changeit

# Export Root CA to server truststore

keytool -exportcert -alias root_ca \
        -keystore ${ROOT_CA_KEYSTORE} -storepass changeit | \
keytool -import -alias root_ca \
        -keystore ${SERVER_TRUST_STORE} -storepass changeit \
        -noprompt -trustcacerts

# Copy files to target project structure

cp -f ${SERVER_IDENTITY_KEYSTORE} ssl-server/src/main/resources
cp -f ${SERVER_TRUST_STORE} ssl-server/src/main/resources
cp -f ${CLIENT_IDENTITY_KEYSTORE} ssl-client/src/main/resources
cp -f ${CLIENT_TRUST_STORE} ssl-client/src/main/resources