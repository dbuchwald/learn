CONTAINERD_VERSION=1.6.6
RUNC_VERSION=1.1.3

mkdir -p tmp

wget https://github.com/containerd/containerd/releases/download/v${CONTAINERD_VERSION}/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz \
     --output-document=tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz

wget https://raw.githubusercontent.com/containerd/containerd/main/containerd.service \
     --output-document=tmp/containerd.service

wget https://github.com/opencontainers/runc/releases/download/v${RUNC_VERSION}/runc.amd64 \
     --output-document=tmp/runc_v${RUNC_VERSION}.amd64

for NODE in c1-master c1-node1 c1-node2 c1-node3
do
  multipass launch --cpus 2 --disk 20G --mem 2G --name $NODE 22.04
  multipass transfer cfg/${NODE}.01-netcfg.yaml ${NODE}:/tmp/01-netcfg.yaml
  multipass exec ${NODE} sudo mv /tmp/01-netcfg.yaml /etc/netplan/
  multipass exec ${NODE} sudo netplan apply
  multipass transfer cfg/hosts ${NODE}:/tmp/hosts
  multipass exec ${NODE} -- sh -c 'cat /tmp/hosts | sudo tee -a /etc/hosts'
  multipass transfer ~/.ssh/id_rsa.pub ${NODE}:/tmp/authorized_keys
  multipass exec ${NODE} -- sh -c 'cat /tmp/authorized_keys | tee -a ~/.ssh/authorized_keys'

  multipass transfer tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz ${NODE}:/tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz
  multipass exec ${NODE} -- sudo tar Czxvf /usr/local /tmp/containerd-${CONTAINERD_VERSION}-linux-amd64.tar.gz

  multipass transfer tmp/containerd.service ${NODE}:/tmp/containerd.service
  multipass exec ${NODE} -- sudo mv /tmp/containerd.service /usr/lib/systemd/system/

  multipass exec ${NODE} -- sudo systemctl daemon-reload
  multipass exec ${NODE} -- sudo systemctl enable --now containerd

  multipass transfer tmp/runc_v${RUNC_VERSION}.amd64 ${NODE}:/tmp/runc_v${RUNC_VERSION}.amd64
  multipass exec ${NODE} -- sudo install -m 755 /tmp/runc_v${RUNC_VERSION}.amd64 /usr/local/sbin/runc

  multipass exec ${NODE} -- sudo mkdir -p /etc/containerd
  multipass exec ${NODE} -- sh -c 'containerd config default | sudo tee /etc/containerd/config.toml'
  multipass exec ${NODE} -- sh -c "sudo sed -i 's/SystemdCgroup \= false/SystemdCgroup \= true/g' /etc/containerd/config.toml"
  multipass exec ${NODE} -- sudo systemctl restart containerd

  multipass transfer cfg/modules-load.d.k8s.conf ${NODE}:/tmp/k8s.conf
  multipass exec ${NODE} -- sudo mv /tmp/k8s.conf /etc/modules-load.d/k8s.conf
  multipass exec ${NODE} -- sudo modprobe overlay
  multipass exec ${NODE} -- sudo modprobe br_netfilter

  multipass transfer cfg/sysctl.d.k8s.conf ${NODE}:/tmp/k8s.conf
  multipass exec ${NODE} -- sudo mv /tmp/k8s.conf /etc/sysctl.d/k8s.conf
  multipass exec ${NODE} -- sudo sysctl --system

  multipass exec ${NODE} -- sudo apt-get update
  multipass exec ${NODE} -- sudo apt-get install -y apt-transport-https ca-certificates curl

  multipass exec ${NODE} -- sudo curl -fsSLo /usr/share/keyrings/kubernetes-archive-keyring.gpg https://packages.cloud.google.com/apt/doc/apt-key.gpg

  multipass exec ${NODE} -- sh -c 'echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list'

  multipass exec ${NODE} -- sudo apt-get update
  multipass exec ${NODE} -- sudo apt-get install -y kubelet kubeadm kubectl
  multipass exec ${NODE} -- sudo apt-mark hold kubelet kubeadm kubectl

done
