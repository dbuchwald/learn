#!/bin/sh

kubectl apply -f apidb-pv.yaml
kubectl apply -f api-namespace.yaml
kubectl apply -f apidb-pvc.yaml
kubectl apply -f apidb-deployment.yaml
kubectl apply -f apidb-service.yaml

kubectl apply -f api-deployment.yaml
kubectl apply -f api-service.yaml
kubectl apply -f api-ingress.yaml

#curl -s http://apiserver.local/api/v1/ref-data/languages | jq
