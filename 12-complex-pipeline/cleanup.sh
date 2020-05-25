#!/bin/sh

kubectl delete -f $(dirname $0)/2-pipeline

for x in p pr t tr; do
  tkn $x delete --all -f
done

oc delete dc,svc,route basic-quarkus-app

kubectl delete build --all

kubectl delete -f $(dirname $0)/3-trigger/4-complex-trigger-template.yml
