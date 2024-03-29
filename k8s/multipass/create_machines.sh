#!/bin/bash

source ./env.sh

if [[ ! -d tmp ]]
then
  echo "Creating tmp folder for all the files..."
  mkdir -p tmp
fi

if [[ ! -f tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz ]]
then
  echo "Downloading containerd runtime..."
  wget https://github.com/containerd/containerd/releases/download/v${CONTAINERD_VERSION}/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz \
       --output-document=tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz
fi

if [[ ! -f tmp/containerd.service ]]
then
  echo "Downloading containerd service config file..."
  wget https://raw.githubusercontent.com/containerd/containerd/main/containerd.service \
       --output-document=tmp/containerd.service
fi 

if [[ ! -f tmp/runc_v${RUNC_VERSION}.amd64 ]]
then
  echo "Downloading RunC runtime..."
  wget https://github.com/opencontainers/runc/releases/download/v${RUNC_VERSION}/runc.amd64 \
       --output-document=tmp/runc_v${RUNC_VERSION}.amd64
fi

if [[ "${CNI_PROVIDER}" == "calico" ]] && [[ ! -f tmp/calico.yaml ]]
then
  echo "Downloading Calico manifest..."
  wget https://projectcalico.docs.tigera.io/manifests/calico.yaml \
       --output-document=tmp/calico.yaml
fi

if [[ "${CNI_PROVIDER}" == "flannel" ]] && [[ ! -f tmp/kube-flannel.yml ]]
then
  echo "Downloading Flannel manifest..."
  wget https://github.com/coreos/flannel/raw/master/Documentation/kube-flannel.yml \
       --output-document=tmp/kube-flannel.yml
fi

for NODE in ${ALL_NODES}
do
  echo "Creating ${NODE} machine..."
  multipass launch --cpus 2 --disk 20G --memory 4G --name $NODE 23.10
# This section was required for static IP address, but testing if can be avoided
#  multipass transfer cfg/${NODE}.01-netcfg.yaml ${NODE}:/tmp/01-netcfg.yaml
#  multipass exec ${NODE} sudo mv /tmp/01-netcfg.yaml /etc/netplan/
#  multipass exec ${NODE} sudo netplan apply
#  multipass transfer cfg/hosts ${NODE}:/tmp/hosts
#  multipass exec ${NODE} -- sh -c 'cat /tmp/hosts | sudo tee -a /etc/hosts'
  echo "Transferring SSH public key for easier authentication..."
  cp ~/.ssh/id_rsa.pub tmp/id_rsa.pub
  multipass transfer tmp/id_rsa.pub ${NODE}:/tmp/authorized_keys
  multipass exec ${NODE} -- sh -c 'cat /tmp/authorized_keys | tee -a ~/.ssh/authorized_keys'

  echo "Installing containerd..."
  multipass transfer tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz ${NODE}:/tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz
  multipass exec ${NODE} -- sudo tar Czxvf /usr/local /tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz

  echo "Installing containerd service..."
  multipass transfer tmp/containerd.service ${NODE}:/tmp/containerd.service
  multipass exec ${NODE} -- sudo mv /tmp/containerd.service /usr/lib/systemd/system/

  echo "Reloading daemons and enabling containerd..."
  multipass exec ${NODE} -- sudo systemctl daemon-reload
  multipass exec ${NODE} -- sudo systemctl enable --now containerd

  echo "Installing RunC..."
  multipass transfer tmp/runc_v${RUNC_VERSION}.amd64 ${NODE}:/tmp/runc_v${RUNC_VERSION}.amd64
  multipass exec ${NODE} -- sudo install -m 755 /tmp/runc_v${RUNC_VERSION}.amd64 /usr/local/sbin/runc

  echo "Configuring containerd for Kubernetes..."
  multipass exec ${NODE} -- sudo mkdir -p /etc/containerd
  multipass exec ${NODE} -- sh -c 'containerd config default | sudo tee /etc/containerd/config.toml'
  multipass exec ${NODE} -- sh -c "sudo sed -i 's/SystemdCgroup \= false/SystemdCgroup \= true/g' /etc/containerd/config.toml"
  multipass exec ${NODE} -- sudo systemctl restart containerd

  echo "Enabling modules for K8S..."
  multipass transfer cfg/modules-load.d.k8s.conf ${NODE}:/tmp/k8s.conf
  multipass exec ${NODE} -- sudo mv /tmp/k8s.conf /etc/modules-load.d/k8s.conf
  multipass exec ${NODE} -- sudo modprobe overlay
  multipass exec ${NODE} -- sudo modprobe br_netfilter

  echo "Enabling Kubernetes system config..."
  multipass transfer cfg/sysctl.d.k8s.conf ${NODE}:/tmp/k8s.conf
  multipass exec ${NODE} -- sudo mv /tmp/k8s.conf /etc/sysctl.d/k8s.conf
  multipass exec ${NODE} -- sudo sysctl --system

  echo "Installing prerequisites for Kubernetes installation..."
  multipass exec ${NODE} -- sudo apt update
  multipass exec ${NODE} -- sudo NEEDRESTART_MODE=a apt install -y apt-transport-https ca-certificates curl

  echo "Installing Kubernetes GPG keys..."
  multipass exec ${NODE} -- sh -c 'sudo curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.28/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg'

  echo "Installing Kubernetes APT repo..."
  multipass exec ${NODE} -- sh -c 'echo "deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.28/deb/ /" | sudo tee /etc/apt/sources.list.d/kubernetes.list'

  echo "Installing Kubernetes binaries..."
  multipass exec ${NODE} -- sudo apt update
  multipass exec ${NODE} -- sudo NEEDRESTART_MODE=a apt install -y kubelet kubeadm kubectl
  multipass exec ${NODE} -- sudo apt-mark hold kubelet kubeadm kubectl

done

echo "All nodes ready, installing K8S cluster..."

echo "Initializing master node..."
# All nodes are ready, init control plane node
multipass exec ${MASTER_NODE} -- sudo kubeadm init ${KUBEADM_INIT_OPTIONS}

echo "Copying Kubernetes configuration file for user..."
multipass exec ${MASTER_NODE} -- sh -c 'mkdir -p $HOME/.kube'
multipass exec ${MASTER_NODE} -- sh -c 'sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config'
multipass exec ${MASTER_NODE} -- sh -c 'sudo chown $(id -u):$(id -g) $HOME/.kube/config'
multipass exec ${MASTER_NODE} -- sh -c 'cp $HOME/.kube/config /tmp/config'
multipass transfer ${MASTER_NODE}:/tmp/config tmp/config
mkdir ~/.kube
cp tmp/config ~/.kube/config

if [[ "${CNI_PROVIDER}" == "calico" ]]
then 
  echo "Installing Calico network plugin..."
  multipass transfer tmp/calico.yaml ${MASTER_NODE}:/tmp/calico.yaml
  multipass exec ${MASTER_NODE} -- sh -c 'kubectl apply -f /tmp/calico.yaml'
fi

if [[ "${CNI_PROVIDER}" == "flannel" ]]
then 
  echo "Installing Flannel network plugin..."
  multipass transfer tmp/kube-flannel.yml ${MASTER_NODE}:/tmp/kube-flannel.yml
  multipass exec ${MASTER_NODE} -- sh -c 'kubectl apply -f /tmp/kube-flannel.yml'
fi

# Get connection details for worker nodes
echo "Retrieving init details..."
CA_CERT_HASH=`multipass exec ${MASTER_NODE} -- openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | openssl dgst -sha256 -hex | sed 's/^.* //'`
KUBE_TOKEN=`multipass exec ${MASTER_NODE} -- kubeadm token list -o jsonpath="{.token}"`

# Make worker nodes join the cluster
echo "Initializing worker nodes node..."
for NODE in ${WORKER_NODES}
do
  echo "Initializing ${NODE}..."
  multipass exec ${NODE} -- sh -c "sudo kubeadm join --token ${KUBE_TOKEN} ${MASTER_NODE}:6443 --discovery-token-ca-cert-hash sha256:${CA_CERT_HASH}"
done
