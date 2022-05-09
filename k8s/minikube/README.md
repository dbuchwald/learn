# Basic k8s deployment objects (to be used with minikube, deprecated and not maintained anymore)

This folder contains all the files required for `kubectl` based deployment of the application using minikube as K8S
provider.

## Development and testing

Some commands that might be useful for local development and testing:

Procedure to deploy API database (MariaDB) in your Kubernetes cluster:

`kubectl apply -f apidb-pv.yaml`

`kubectl apply -f apidb-pvc.yaml`

`kubectl apply -f apidb-service.yaml`

`kubectl apply -f apidb-deployment.yaml`

Procedure to deploy API application in your Kubernetes cluster is a bit more complicated. Start with
building Docker image for minikube:

`eval $(minikube docker-env)` - this will set up environment variables for communication with minikube 
Docker instance

`cd ../apps/api/`

`docker build -f Dockerfile.standalone -t dawidbuchwald/k8s-learn-api-v1:latest .` - build Docker image
for the minikube to use

`cd ../../deploy`

`kubectl apply -f api-service.yaml`

`kubectl apply -f api-deployment.yaml`

`minikube service api --url`

Open provided URL to test the application.