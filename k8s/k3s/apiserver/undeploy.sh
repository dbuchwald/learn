#!/bin/sh

kubectl delete -f api-ingress.yaml
kubectl delete -f api-service.yaml
kubectl delete -f api-deployment.yaml
kubectl delete -f api-configmap.yaml

kubectl delete -f apidb-service.yaml
kubectl delete -f apidb-deployment.yaml
kubectl delete -f apidb-secret.yaml
kubectl delete -f apidb-configmap.yaml
kubectl delete -f apidb-pvc.yaml
kubectl delete -f api-namespace.yaml
