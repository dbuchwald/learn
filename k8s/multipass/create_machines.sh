CONTAINERD_VERSION=1.6.6
RUNC_VERSION=1.1.3
MASTER_NODE="c1-master"
WORKER_NODES="c1-node1 c1-node2 c1-node3"

ALL_NODES="${MASTER_NODE} ${WORKER_NODES}"

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

if [[ ! -f tmp/calico.yaml ]]
then
  echo "Downloading Calico manifest..."
  wget https://projectcalico.docs.tigera.io/manifests/calico.yaml \
       --output-document=tmp/calico.yaml
fi

for NODE in ${ALL_NODES}
do
  echo "Creating ${NODE} machine..."
  multipass launch --cpus 2 --disk 20G --mem 2G --name $NODE 22.04
# This section was required for static IP address, but testing if can be avoided
#  multipass transfer cfg/${NODE}.01-netcfg.yaml ${NODE}:/tmp/01-netcfg.yaml
#  multipass exec ${NODE} sudo mv /tmp/01-netcfg.yaml /etc/netplan/
#  multipass exec ${NODE} sudo netplan apply
#  multipass transfer cfg/hosts ${NODE}:/tmp/hosts
#  multipass exec ${NODE} -- sh -c 'cat /tmp/hosts | sudo tee -a /etc/hosts'
  echo "Transferring SSH public key for easier authentication..."
  multipass transfer ~/.ssh/id_rsa.pub ${NODE}:/tmp/authorized_keys
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
  multipass exec ${NODE} -- sudo apt-get update
  multipass exec ${NODE} -- sudo apt-get install -y apt-transport-https ca-certificates curl

  echo "Installing Kubernetes GPG keys..."
  multipass exec ${NODE} -- sudo curl -fsSLo /usr/share/keyrings/kubernetes-archive-keyring.gpg https://packages.cloud.google.com/apt/doc/apt-key.gpg

  echo "Installing Kubernetes APT repo..."
  multipass exec ${NODE} -- sh -c 'echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list'

  echo "Installing Kubernetes binaries..."
  multipass exec ${NODE} -- sudo apt-get update
  multipass exec ${NODE} -- sudo apt-get install -y kubelet kubeadm kubectl
  multipass exec ${NODE} -- sudo apt-mark hold kubelet kubeadm kubectl

done

echo "All nodes ready, installing K8S cluster..."

echo "Initializing master node..."
# All nodes are ready, init control plane node
multipass exec ${MASTER_NODE} -- sudo kubeadm init

echo "Copying Kubernetes configuration file for user..."
multipass exec ${MASTER_NODE} -- sh -c 'mkdir -p $HOME/.kube'
multipass exec ${MASTER_NODE} -- sh -c 'sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config'
multipass exec ${MASTER_NODE} -- sh -c 'sudo chown $(id -u):$(id -g) $HOME/.kube/config'

echo "Installing Pod network plugin..."
multipass transfer tmp/calico.yaml ${MASTER_NODE}:/tmp/calico.yaml
multipass exec ${MASTER_NODE} -- sh -c 'kubectl apply -f /tmp/calico.yaml'

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
