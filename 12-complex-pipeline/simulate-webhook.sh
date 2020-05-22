#/bin/sh

http POST complex-pipeline-listener-interceptor-pipelines-tutorial.apps.mw.consulting-rh-br.com \
    'Content-Type':'application/json' @$(dirname $0)/gitpush.payload
tkn p logs -a -L -f