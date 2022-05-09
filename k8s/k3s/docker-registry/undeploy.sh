#!/bin/sh

kubectl delete -f docker-ingress.yaml
kubectl delete -f docker-service.yaml
kubectl delete -f docker-deployment.yaml
kubectl delete -f docker-pvc.yaml
kubectl delete -f docker-namespace.yaml
kubectl delete -f docker-pv.yaml
