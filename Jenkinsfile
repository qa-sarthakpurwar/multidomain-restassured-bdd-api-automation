```groovy
pipeline {
    agent any

    parameters {
        string(name: 'RUN', defaultValue: '@ECommerce,@GooglePlace', description: 'Tags to execute (comma separated)')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                script {

                    def tags = params.RUN.split(",")

                    for (int i = 0; i < tags.size(); i++) {

                        def tag = tags[i].trim()
                        def cleanTag = tag.replace('@','')

                        echo "Running for tag: ${tag}"

                        bat "mvn clean test -Dcucumber.filter.tags=\"${tag}\" -Dreport.name=${cleanTag}"
                    }
                }
            }
        }

        stage('Parse Cucumber Report') {
            steps {
                script {

                    def filePath = "target/jsonReports/cucumber.json"

                    echo "Looking for file: ${filePath}"

                    if (!fileExists(filePath)) {
                        error "Cucumber JSON report not found!"
                    }

                    def jsonText = readFile(filePath)
                    def report = new groovy.json.JsonSlurper().parseText(jsonText)

                    int total = 0
                    int passed = 0
                    int failed = 0
                    int skipped = 0

                    report.each { feature ->
                        feature.elements.each { scenario ->
                            total++

                            def statuses = scenario.steps.collect { it?.result?.status }

                            if (statuses.contains("failed")) {
                                failed++
                            } else if (statuses.contains("skipped")) {
                                skipped++
                            } else {
                                passed++
                            }
                        }
                    }

                    env.TOTAL = total.toString()
                    env.PASSED = passed.toString()
                    env.FAILED = failed.toString()
                    env.SKIPPED = skipped.toString()
                }
            }
        }
    }

    post {
        always {

            archiveArtifacts artifacts: 'target/**/*.*', allowEmptyArchive: true

            emailext(
                to: 'qa.sarthakpurwar@gmail.com',
                subject: "🚀 Build #${env.BUILD_NUMBER} | ${currentBuild.currentResult}",

                body: """
                <html>
                <body style="font-family:Arial;">

                <h2>🚀 Automation Execution Summary</h2>

                <table border="1" cellpadding="8" cellspacing="0" style="width:60%; text-align:center;">
                    <tr>
                        <th>Total</th>
                        <th>Passed</th>
                        <th>Failed</th>
                        <th>Skipped</th>
                    </tr>
                    <tr>
                        <td>${env.TOTAL ?: '0'}</td>
                        <td>${env.PASSED ?: '0'}</td>
                        <td>${env.FAILED ?: '0'}</td>
                        <td>${env.SKIPPED ?: '0'}</td>
                    </tr>
                </table>

                <br>

                <a href="${env.BUILD_URL}artifact/target/">View Reports</a>

                </body>
                </html>
                """,

                mimeType: 'text/html',
                attachmentsPattern: 'target/*ExtentReport*.html'
            )
        }
    }
}
```
