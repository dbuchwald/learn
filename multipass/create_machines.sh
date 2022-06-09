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
done
