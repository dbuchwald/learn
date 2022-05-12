# Deployment notes for K3S

This folder contains all the files required for `kubectl` based deployment of the application using K3S provider.

## Initial K3S cluster setup

Follow basic installation procedure refer to the [documentation](https://rancher.com/docs/k3s/latest/en/installation/install-options/).

**PLEASE NOTE**: if running any RedHat-based Linux distro (RHEL/CentOS/Fedora Workstation) you have to
disable **firewalld** service for the below to work, according to the documentation. That being said, the following
settings were sufficient to make it work with `firewalld` enabled (assuming FedoraWorkstation is the default zone):

```shell
sudo firewall-cmd --get-default-zone
FedoraWorkstation

sudo firewall-cmd --permanent --add-port=6443/tcp # flannel
sudo firewall-cmd --permanent --zone=trusted --add-source=10.42.0.0/16 # pods
sudo firewall-cmd --permanent --zone=trusted --add-source=10.43.0.0/16 # services
sudo firewall-cmd --permanent --zone=FedoraWorkstation --add-service=http
sudo firewall-cmd --permanent --zone=FedoraWorkstation --add-service=https
sudo firewall-cmd --reload
```

**PLEASE NOTE**: Reloading `firewalld` configuration will erase any runtime `iptables`
changes introduced by `svclb-traefik`, preventing Ingress operation. You will notice
that any attempt to connect to `localhost:80` will fail with "Connection refused", because
even with the Ingress running, there is nothing listening on port 80/443. The way traffic
is directed to Traefik is using runtime change implemented in iptables when DaemonSet 
`svclb-traefik` is deployed.

Therefore, after `firewalld` configuration reload you might not be able to use Ingress
without restart. There is, however, other method: restart DaemonSet - it will inject
iptables configuration again, restoring connectivity:

```shell
kubectl rollout restart -n kube-system ds/svclb-traefik
```

## Create your own root CA and domain certificates

Go to `learn/k8s/ca` folder and run `gen_root_ca.sh` script to generate root CA certificate that will be used to sign
all the domain certificates used later on:

```shell
./gen_root_ca.sh 
Generating root CA key, please create password when prompted...
Enter PEM pass phrase:
Verifying - Enter PEM pass phrase:
Generating root CA certificate, please enter the password when prompted...
Enter pass phrase for kubernetes.local.root.ca.key:
Root CA certificate created, import it into OS keystore
```

Make sure to remember the password, it will be needed whenever you need to generate new domain certificate.

To be able to trust this newly created CA (and all domains signed by it), import root CA into OS truststore. For
Fedora 36 it will be the following sequence of commands:

```shell
sudo cp kubernetes.local.root.ca.pem /etc/pki/ca-trust/source/anchors/
sudo update-ca-trust
```

For other OSes it might differ. 

**PLEASE NOTE**: Chrome browser, by default, doesn't use system CA store, so this
certificate will have to be imported into application manually. This might apply to other browsers as well.

Now, generate keys and certificates to be used by the domains: `docker-registry.local` and `apiserver.local`:

```shell
./gen_domain_cert.sh docker-registry.local
Generating certificate private key...
Generating certificate request file...
Generating certificate request extension file...
Generating certificate, please enter root CA password when prompted...
Certificate request self-signature ok
subject=C = PL, ST = Masovia, L = Warsaw, O = Home, CN = docker-registry.local
Enter pass phrase for kubernetes.local.root.ca.key:
```

```shell
./gen_domain_cert.sh apiserver.local
Generating certificate private key...
Generating certificate request file...
Generating certificate request extension file...
Generating certificate, please enter root CA password when prompted...
Certificate request self-signature ok
subject=C = PL, ST = Masovia, L = Warsaw, O = Home, CN = apiserver.local
Enter pass phrase for kubernetes.local.root.ca.key:
```

You expect to see the following files in your folder:

```shell
ls -l
total 48
-rw-r--r--. 1 dawid dawid 1350 May 12 22:57 apiserver.local.crt
-rw-r--r--. 1 dawid dawid  985 May 12 22:57 apiserver.local.csr
-rw-r--r--. 1 dawid dawid  206 May 12 22:57 apiserver.local.ext
-rw-------. 1 dawid dawid 1704 May 12 22:57 apiserver.local.key
-rw-r--r--. 1 dawid dawid 1367 May 12 22:56 docker-registry.local.crt
-rw-r--r--. 1 dawid dawid  993 May 12 22:56 docker-registry.local.csr
-rw-r--r--. 1 dawid dawid  212 May 12 22:56 docker-registry.local.ext
-rw-------. 1 dawid dawid 1704 May 12 22:56 docker-registry.local.key
-rwxr-xr-x. 1 dawid dawid 1127 May 12 22:54 gen_domain_cert.sh
-rwxr-xr-x. 1 dawid dawid  464 May 12 22:42 gen_root_ca.sh
-rw-------. 1 dawid dawid 1854 May 12 22:56 kubernetes.local.root.ca.key
-rw-r--r--. 1 dawid dawid 1302 May 12 22:56 kubernetes.local.root.ca.pem
```

These files will be picked up by the deployment scripts later on. Please, keep the files intact and do not regenerate
them unless you are willing to import new root CA into your system repeatedly.

## Deployment of private Docker registry

When cluster is up and running, you need to set up private Docker registry, and it will be running inside
the K3S cluster itself. It is assumed that the repository name will be **docker-registry.local**, but it 
can be changed, as long as all the occurrences are replaced.

Start with creation of all the directories used for Persistent Volume of Docker registry:

`sudo mkdir -p /var/k3s/docker-registry/registry`

You will also need to add entry to `/etc/hosts`:

`127.0.0.1   docker-registry.local`

Follow with installation of the docker registry, either by running `docker-registry/deploy.sh` script
or running the commands manually:

```shell
cd docker-registry
kubectl apply -f docker-pv.yaml
kubectl apply -f docker-namespace.yaml
kubectl apply -f docker-pvc.yaml
kubectl create secret tls -n docker-registry docker-registry-tls --cert=../../ca/docker-registry.local.crt --key=../../ca/docker-registry.local.key
kubectl apply -f docker-deployment.yaml
kubectl apply -f docker-service.yaml
kubectl apply -f docker-ingress.yaml
cd ..
```

Verify setup by running `kubectl -n docker-registry get all` command. You expect similar output:

```
NAME                                  READY   STATUS    RESTARTS   AGE
pod/docker-registry-d87b8b454-2dxxq   1/1     Running   0          58m

NAME                              TYPE       CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
service/docker-registry-service   NodePort   10.43.70.200   <none>        5000:30005/TCP   58m

NAME                              READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/docker-registry   1/1     1            1           58m

NAME                                        DESIRED   CURRENT   READY   AGE
replicaset.apps/docker-registry-d87b8b454   1         1         1       58m
```

Make sure to investigate the Ingress (`kubectl -n docker-registry get ingress`) and PVC 
(`kubectl -n docker-registry get pvc`) objects:

```
NAME                      CLASS    HOSTS                   ADDRESS         PORTS   AGE
docker-registry-ingress   <none>   docker-registry.local   192.168.1.239   80      59m

NAME                  STATUS   VOLUME                      CAPACITY   ACCESS MODES   STORAGECLASS   AGE
docker-registry-pvc   Bound    docker-registry-pv-volume   10Gi       RWO                           59m
```

If all is running fine, you expect this command to work correctly: `curl -s https://docker-registry.local/v2/`

```json
{}
```

> In case you run into any issues with the Ingress whatsoever, you can use the `ingress-test` folder
> for analysis and troubleshooting - it contains sample YAML files for deployment of minimal Apache-based
> application with Service and Ingress configured.

You can now build and tag the Docker image to be used by the API application:

```shell
docker build -f ../apps/api/Dockerfile.standalone -t docker-registry.local/k8s-learn-api-v1:latest ../apps/api
```

When completed, you can push the image to your private registry:

```shell
docker push docker-registry.local/k8s-learn-api-v1:latest
```

Operation should be successful, and to confirm, you can query the catalog using `curl` again:

```shell
curl -s https://docker-registry.local/v2/_catalog
```

```json
{"repositories":["k8s-learn-api-v1"]}
```

## API Application deployment

Start by creating filesystem location for Persistent Volume:

`sudo mkdir -p /var/k3s/api/apidb`

You can deploy the application using `apiserver/deploy.sh` script or execute commands manually:

```shell
cd apiserver
kubectl apply -f apidb-pv.yaml
kubectl apply -f api-namespace.yaml
kubectl apply -f apidb-storageclass.yaml
kubectl apply -f apidb-pvc.yaml
kubectl apply -f apidb-configmap.yaml
kubectl apply -f apidb-secret.yaml
kubectl apply -f apidb-deployment.yaml
kubectl apply -f apidb-service.yaml

kubectl apply -f api-configmap.yaml
kubectl create secret tls -n api apiserver-tls --cert=../../ca/apiserver.local.crt --key=../../ca/apiserver.local.key
kubectl apply -f api-deployment.yaml
kubectl apply -f api-service.yaml
kubectl apply -f api-ingress.yaml
cd ..
```

Verify the deployment with the following command: `kubectl -n api get all`

```
NAME                         READY   STATUS    RESTARTS      AGE
pod/apidb-59f549b89f-njsbf   1/1     Running   0             36m
pod/api-58bdcf9889-5rwm2     1/1     Running   2 (36m ago)   36m

NAME                    TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)    AGE
service/apidb-service   ClusterIP   None           <none>        3306/TCP   36m
service/api-service     ClusterIP   10.43.204.35   <none>        8080/TCP   36m

NAME                    READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/apidb   1/1     1            1           36m
deployment.apps/api     1/1     1            1           36m

NAME                               DESIRED   CURRENT   READY   AGE
replicaset.apps/apidb-59f549b89f   1         1         1       36m
replicaset.apps/api-58bdcf9889     1         1         1       36m
```

You can verify service operation **only if you are running K3S locally** simply by calling using ClusterIP:

```shell
curl -s http://10.43.204.35:8080/api/v1/ref-data/languages
```

```json
[{"langId":"en","description":"English"},{"langId":"es","description":"Español"},{"langId":"pl","description":"Polski"}]
```

Check the Ingress and PVC as well:

```
kubectl -n api get ingress
NAME          CLASS    HOSTS             ADDRESS         PORTS   AGE
api-ingress   <none>   apiserver.local   192.168.1.239   80      37m

kubectl -n api get pvc
NAME             STATUS   VOLUME            CAPACITY   ACCESS MODES   STORAGECLASS   AGE
apidb-pv-claim   Bound    apidb-pv-volume   1Gi        RWO                           38m
```

Since Docker registry and API server use the same Ingress on port 80, you need to route requests to specific
services using the `Host` header. You could do the following:

```shell
curl --insecure --header "Host: apiserver.local" https://localhost/api/v1/ref-data/languages
```

```json
[{"langId":"en","description":"English"},{"langId":"es","description":"Español"},{"langId":"pl","description":"Polski"}]
```

But to make it easier, you can edit `/etc/hosts` again, by adding:

```
127.0.0.1   apiserver.local
```

Making the calls easier:

```
curl https://apiserver.local/api/v1/ref-data/languages
```

You can also test the application in your browser using dedicated web page <https://apiserver.local>

## Cleaning up

You can remove all the created artifacts by running `undeploy.sh` scripts in `apiserver` and 
`docker-registry` folders. 

**Please note**: this will not remove entries in `/etc/docker/daemon.json`, `/etc/hosts` nor
`/etc/rancher/k3s/registries.yaml`. You have to revert these files manually.