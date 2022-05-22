#!/bin/bash
CA_ROOT_FOLDER=/var/k3s/ca
SYSTEM_CA_CERTS_FOLDER=/usr/local/share/ca-certificates/
K3S_CA_CERT_FOLDER=${SYSTEM_CA_CERTS_FOLDER}/k3s

# Start with system update
sudo apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get upgrade -y

# Remove old version of Docker packages
sudo apt-get remove -y docker docker-engine docker.io containerd runc

# Install objects required for Docker/Kubectl installation
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y ca-certificates curl gnupg lsb-release apt-transport-https openssl

# Install Docker repo GPG key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# Install Docker repository
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker engine
sudo apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Install kubectl repo GPG key
sudo curl -fsSLo /usr/share/keyrings/kubernetes-archive-keyring.gpg https://packages.cloud.google.com/apt/doc/apt-key.gpg

# Install kubectl repository
echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list > /dev/null

# Install kubectl
sudo apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y kubectl

# Install Helm repo GPG key
curl https://baltocdn.com/helm/signing.asc | sudo apt-key add -

# Install Helm repository
echo "deb https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list > /dev/null

# Install helm
sudo apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y helm

# Generate Root CA
sudo mkdir -p ${CA_ROOT_FOLDER}
sudo openssl genrsa -out ${CA_ROOT_FOLDER}/k3s.local.root.ca.key 2048
sudo openssl req -x509 -new -nodes -key ${CA_ROOT_FOLDER}/k3s.local.root.ca.key -sha256 -days 1825 -out ${CA_ROOT_FOLDER}/k3s.local.root.ca.pem -subj "/C=PL/ST=Masovia/L=Warsaw/O=Home/CN=k3s.local"

# Import Root CA into system store
sudo mkdir ${K3S_CA_CERT_FOLDER}
sudo chmod 755 ${K3S_CA_CERT_FOLDER}
sudo cp ${CA_ROOT_FOLDER}/k3s.local.root.ca.pem ${K3S_CA_CERT_FOLDER}/k3s.local.root.ca.crt
sudo chmod 644 ${K3S_CA_CERT_FOLDER}/k3s.local.root.ca.crt
sudo update-ca-certificates

# Install K3S software
curl -sfL https://get.k3s.io | sh -

# Copy kubectl config file for easier kubectl usage
mkdir ~/.kube
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chown $USER:$USER ~/.kube/config

# Install Helm repo for cert-manager
helm repo add jetstack https://charts.jetstack.io

# Update Helm repositories
helm repo update

# Install cert-manager
helm install \
  cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --version v1.8.0 \
  --set installCRDs=true

# Generate secret file
ROOT_CA_CRT=`sudo cat ${CA_ROOT_FOLDER}/k3s.local.root.ca.pem | base64 -w0`
ROOT_CA_KEY=`sudo cat ${CA_ROOT_FOLDER}/k3s.local.root.ca.key | base64 -w0`

cat <<EOF > k3s-root-ca.secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: k3s-root-ca
  namespace: cert-manager
data:
  tls.crt: ${ROOT_CA_CRT} 
  tls.key: ${ROOT_CA_KEY}
EOF

# Install secret
kubectl apply -f k3s-root-ca.secret.yaml

# Generate ClusterIssuer object
cat <<EOF > k3s-root-ca.clusterissuer.yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: k3s-root-ca
  namespace: cert-manager
spec:
  ca:
    secretName: k3s-root-ca
EOF

# Install ClusterIssuer object
kubectl apply -f k3s-root-ca.clusterissuer.yaml


