#!/bin/sh

kubectl delete -f $(dirname $0)

for x in p pr t tr; do
  tkn $x delete --all -f
done

oc delete dc,svc,route basic-quarkus-app

kubectl delete build --all

kubectl delete -f $(dirname $0)/4-triggers/4-complex-trigger-template.yml
