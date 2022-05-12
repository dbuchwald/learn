#!/bin/sh
echo "Generating root CA key, please create password when prompted..."
openssl genrsa -des3 -out kubernetes.local.root.ca.key 2048
echo "Generating root CA certificate, please enter the password when prompted..."
openssl req -x509 -new -nodes -key kubernetes.local.root.ca.key -sha256 -days 1825 -out kubernetes.local.root.ca.pem -subj "/C=PL/ST=Masovia/L=Warsaw/O=Home/CN=kubernetes.local"
echo "Root CA certificate created, import it into OS keystore"
