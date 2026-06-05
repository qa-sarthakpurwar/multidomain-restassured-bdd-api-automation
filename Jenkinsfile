pipeline {
    agent any

    parameters {
        string(name: 'RUN', defaultValue: '@ECommerce,@GooglePlace', description: 'Tags to execute (comma separated)')
    }

    stages {

        stage('Run Tests in Parallel') {
            steps {
                script {

                    def tags = params.RUN.split(",")
                    def jobs = [:]

                    for (int i = 0; i < tags.size(); i++) {

                        def tag = tags[i].trim()
                        def cleanTag = tag.replace('@','')

                        jobs["Run ${tag}"] = {

                            dir("run-${cleanTag}") {

                                // Fresh checkout for each parallel run
                                checkout scm

                                echo "Running for tag: ${tag}"

                                bat "mvn clean test -Dcucumber.filter.tags=\"${tag}\" -Dreport.name=${cleanTag}"

                            }
                        }
                    }

                    parallel jobs
                }
            }
        }

        stage('Parse Reports') {
            steps {
                script {

                    def tags = params.RUN.split(",")

                    int total = 0
                    int passed = 0
                    int failed = 0
                    int skipped = 0

                    tags.each { tag ->

                        def cleanTag = tag.replace('@','')
                        def filePath = "run-${cleanTag}/target/jsonReports/cucumber.json"

                        echo "Reading: ${filePath}"

                        if (!fileExists(filePath)) {
                            echo "WARNING: Missing report for ${tag}"
                            return
                        }

                        def jsonText = readFile(filePath)
                        def report = new groovy.json.JsonSlurper().parseText(jsonText)

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

            // Archive ALL reports from all runs
            archiveArtifacts artifacts: '**/target/**/*.*', allowEmptyArchive: true

            emailext(
                to: 'qa.sarthakpurwar@gmail.com',
                subject: "🚀 Build #${env.BUILD_NUMBER} | ${currentBuild.currentResult}",

                body: """
                <html>
                <body>

                <h2>🚀 Parallel Execution Summary</h2>

                <table border="1" cellpadding="8" cellspacing="0">
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

                <a href="${env.BUILD_URL}artifact/">📊 View All Reports</a>

                </body>
                </html>
                """,

                mimeType: 'text/html',
                attachmentsPattern: '**/*ExtentReport*.html'
            )
        }
    }
}