#!/bin/sh
DOMAIN_NAME=$1

if [ "$DOMAIN_NAME" = "" ] 
then
  echo "Please enter domain name as a parameter, eg. $0 docker-registry.local"
  exit 1
fi

echo "Generating certificate private key..."
openssl genrsa -out $DOMAIN_NAME.key 2048

echo "Generating certificate request file..."
openssl req -new -key $DOMAIN_NAME.key -out $DOMAIN_NAME.csr -subj "/C=PL/ST=Masovia/L=Warsaw/O=Home/CN=${DOMAIN_NAME}"

echo "Generating certificate request extension file..."

echo "authorityKeyIdentifier=keyid,issuer" > $DOMAIN_NAME.ext
echo "basicConstraints=CA:FALSE" >> $DOMAIN_NAME.ext
echo "keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment" >> $DOMAIN_NAME.ext
echo "subjectAltName = @alt_names" >> $DOMAIN_NAME.ext
echo "" >> $DOMAIN_NAME.ext
echo "[alt_names]" >> $DOMAIN_NAME.ext
echo "DNS.1 = ${DOMAIN_NAME}" >> $DOMAIN_NAME.ext

echo "Generating certificate, please enter root CA password when prompted..."
openssl x509 -req -in $DOMAIN_NAME.csr -CA kubernetes.local.root.ca.pem -CAkey kubernetes.local.root.ca.key -CAcreateserial -out $DOMAIN_NAME.crt -days 825 -sha256 -extfile $DOMAIN_NAME.ext 

