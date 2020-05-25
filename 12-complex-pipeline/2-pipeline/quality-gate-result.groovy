import groovy.json.JsonSlurper

def readPropertiesFile(debug, propertiesFilePath) {
    if (!propertiesFilePath)
        throw new RuntimeException("Param reportPath cannot be null")

    if (debug.toBoolean())
        println("Reading the file: ${propertiesFilePath}")

    def file = new File(propertiesFilePath) 
    
    if (!file.exists())
        throw new RuntimeException("File does not exist!")
    
    def properties = new Properties()

    file.withInputStream {
        properties.load(it)
    }

    properties
}

def queryRestEndpoint(debug, endpointUri) {
    if (!endpointUri)
        throw new RuntimeException("Param endpointUri cannot be null")

    if (debug.toBoolean())
        println("Querying the endpoint: ${endpointUri}")

    def connection = new URL(endpointUri)
            .openConnection() as HttpURLConnection

    connection.setRequestProperty( 'User-Agent', 'groovy' )
    connection.setRequestProperty( 'Accept', 'application/json' )

    if ( connection.responseCode == 200 ) {
        def json = connection.inputStream.withCloseable { inStream ->
            new JsonSlurper().parse( inStream as InputStream )
        }

        if (debug.toBoolean()) 
            println("Result from endpoint ${endpointUri}: ${json}")

        json
    } else {
        println connection.responseCode + ": " + connection.inputStream.text
        null
    }
}

def getTaskResult(reportPath, sonarEndpointUri, debug) {
    if (!reportPath || !sonarEndpointUri)
        throw new RuntimeException("Params reportPath and endpointUri cannot be null")

    if (debug.toBoolean()) {
        println("reportPath=${reportPath}")
        println("sonarEndpointUri=${sonarEndpointUri}")
    }

    def properties = readPropertiesFile(debug, reportPath)
    def taskResult = queryRestEndpoint(debug, "${sonarEndpointUri}/api/ce/task?id=${properties.ceTaskId}")
    def result = queryRestEndpoint(debug, "${sonarEndpointUri}/api/qualitygates/project_status?analysisId=${taskResult.task.analysisId}")
    
    println("Quality Gate: ${result.projectStatus.status}")

    if (result.projectStatus.status == 'OK') {
        System.exit(0)
    }

    System.exit(1)
}

getTaskResult(this.args[0], this.args[1], this.args[2])