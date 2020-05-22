kubectl get taskrun/results-taskrun \
    -o yaml | yq r - \
    'status.steps[*].terminated.message'