#/bin/sh

http POST http://e0c7a4b3.ngrok.io \
    'Content-Type':'application/json' @$(dirname $0)/gitpush.payload
tkn p logs -a -L -f