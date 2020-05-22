#/bin/sh

http POST http://el-github-listener-interceptor-pipelines-tutorial.apps-crc.testing 'Content-Type':'application/json' @gitpush.payload
