#!/bin/sh

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

#curl -s https://apiserver.local/api/v1/ref-data/languages | jq
