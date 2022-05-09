#!/bin/sh

kubectl apply -f docker-pv.yaml
kubectl apply -f docker-namespace.yaml
kubectl apply -f docker-pvc.yaml
kubectl apply -f docker-deployment.yaml
kubectl apply -f docker-service.yaml
kubectl apply -f docker-ingress.yaml

#docker build -f ../../apps/api/Dockerfile.standalone -t docker-registry.local/k8s-learn-api-v1:latest ../../apps/api
#docker push docker-registry.local/k8s-learn-api-v1:latest