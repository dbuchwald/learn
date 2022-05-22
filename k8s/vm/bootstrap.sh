#!/bin/bash
CA_ROOT_FOLDER=/var/k3s/ca
SYSTEM_CA_CERTS_FOLDER=/usr/local/share/ca-certificates/
K3S_CA_CERT_FOLDER=${SYSTEM_CA_CERTS_FOLDER}/k3s
DOCKER_REGISTRY_ROOT_FOLDER=/var/k3s/docker-registry

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

# Add user to docker group 
sudo usermod -aG docker $USER

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

# Generate secret object
ROOT_CA_CRT=`sudo cat ${CA_ROOT_FOLDER}/k3s.local.root.ca.pem | base64 -w0`
ROOT_CA_KEY=`sudo cat ${CA_ROOT_FOLDER}/k3s.local.root.ca.key | base64 -w0`

cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: Secret
metadata:
  name: k3s-root-ca
  namespace: cert-manager
data:
  tls.crt: ${ROOT_CA_CRT} 
  tls.key: ${ROOT_CA_KEY}
EOF

# Generate ClusterIssuer object
cat <<EOF | kubectl apply -f -
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: k3s-root-ca
  namespace: cert-manager
spec:
  ca:
    secretName: k3s-root-ca
EOF

# Create folder for Docker registry
sudo mkdir -p ${DOCKER_REGISTRY_ROOT_FOLDER}

# Create entry in /etc/hosts file (inside VM)
echo "127.0.0.1 docker-registry.local" | sudo tee -a /etc/hosts >/dev/null

# Create Docker registry yaml file (with all the objects bundled)
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: PersistentVolume
metadata:
  name: docker-registry-volume
  namespace: docker-registry
  labels:
    type: local
spec:
  storageClassName: 
  capacity:
    storage: 10Gi
  accessModes:
    - ReadWriteOnce
  hostPath:
    path: "${DOCKER_REGISTRY_ROOT_FOLDER}"
  claimRef:
    apiVersion: v1
    kind: PersistentVolumeClaim
    name: docker-registry-pvc
    namespace: docker-registry

---

kind: Namespace
apiVersion: v1
metadata:
  name: docker-registry
  labels:
    name: docker-registry

---

apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: docker-registry-pvc
  namespace: docker-registry
spec:
  accessModes:
    - ReadWriteOnce
  storageClassName: 
  volumeName: docker-registry-volume
  resources:
    requests:
      storage: 10Gi

---

apiVersion: apps/v1
kind: Deployment
metadata:
  name: docker-registry
  namespace: docker-registry
spec:
  replicas: 1
  selector:
    matchLabels:
      app: docker-registry
  template:
    metadata:
      labels:
        app: docker-registry
        name: docker-registry
    spec:
      containers:
      - name: docker-registry
        image: registry:2
        ports:
        - containerPort: 5000
        volumeMounts:
        - name: registry-volume
          mountPath: /var/lib/registry
          subPath: registry
      volumes:
        - name: registry-volume
          persistentVolumeClaim:
            claimName: docker-registry-pvc

---

apiVersion: v1
kind: Service
metadata:
  name: docker-registry-service
  namespace: docker-registry
  labels:
    run: docker-registry
spec:
  type: ClusterIP
  selector:
    app: docker-registry
  ports:
    - protocol: TCP
      port: 5000
      name: docker-registry

---

apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: docker-registry-ingress
  namespace: docker-registry
  annotations:
    kubernetes.io/ingress.class: "traefik"
    cert-manager.io/cluster-issuer: k3s-root-ca
spec:
  tls:
  - hosts:
    - docker-registry.local
    secretName: docker-registry-tls
  rules:
  - host: docker-registry.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: docker-registry-service
            port:
              name: docker-registry

EOF

