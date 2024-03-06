CONTAINERD_VERSION=1.7.13
RUNC_VERSION=1.1.12
KUBEADM_INIT_OPTIONS=

if [[ "${CLUSTER_NAME}" == "" ]]
then
  CLUSTER_NAME="c1"
fi

if [[ "${CNI_PROVIDER}" == "" ]]
then
  CNI_PROVIDER="calico"
fi

if [[ "${CNI_PROVIDER}" != "flannel" ]] && [[ "${CNI_PROVIDER}" != "calico" ]] && [[ "${CNI_PROVIDER}" != "none" ]]
then
  echo "Provided value of CNI_PROVIDER variable (${CNI_PROVIDER}) is not valid, none will be used"
  CNI_PROVIDER="none"
fi

if [[ "${CNI_PROVIDER}" == "flannel" ]]
then
#  KUBEADM_INIT_OPTIONS+="--pod-network-cidr=10.244.0.0/16 --service-cidr=10.245.0.0/24"
  KUBEADM_INIT_OPTIONS+="--pod-network-cidr=10.244.0.0/16"
fi

MASTER_NODE="${CLUSTER_NAME}-master"
WORKER_NODES="${CLUSTER_NAME}-node1 ${CLUSTER_NAME}-node2 ${CLUSTER_NAME}-node3"

ALL_NODES="${MASTER_NODE} ${WORKER_NODES}"

echo "Installing on (${ALL_NODES}) using ${CNI_PROVIDER}"
