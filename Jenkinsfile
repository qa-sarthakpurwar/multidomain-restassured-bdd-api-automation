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

        stage('Run Tests in Parallel') {
            steps {
                script {

                    def tags = params.RUN.split(",")

                    def jobs = [:]

                    for (int i = 0; i < tags.size(); i++) {

                        def tag = tags[i].trim()

                        jobs["Run ${tag}"] = {
                          bat """
mvn clean test ^
-Dcucumber.filter.tags="${tag}" ^
-Dreport.name=${tag.replace('@','')} ^
-Dcucumber.plugin=json:target/jsonReports/${tag.replace('@','')}.json
"""
                        }
                    }

                    parallel jobs
                }
            }
        }

        stage('Parse Cucumber Report') {
            steps {
                script {

                    def tags = params.RUN.split(",")

                    int total = 0
                    int passed = 0
                    int failed = 0
                    int skipped = 0

                    tags.each { tag ->

                        def filePath = "target/jsonReports/${tag.trim()}.json"

                        if (!fileExists(filePath)) {
                            error "Cucumber JSON report not found at: ${filePath}"
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

            archiveArtifacts artifacts: 'target/**/*.*', allowEmptyArchive: true

            emailext(
                to: 'qa.sarthakpurwar@gmail.com',
                subject: "🚀 Build #${env.BUILD_NUMBER} | ${currentBuild.currentResult}",

                body: """
                <html>
                <body style="font-family:Arial;">

                <h2 style="color:#2c3e50;">🚀 Automation Execution Summary</h2>

                <hr>

                <table border="1" cellpadding="8" cellspacing="0"
                       style="border-collapse: collapse; width: 60%; text-align: center;">

                    <tr style="background-color:#2c3e50; color:white;">
                        <th>Total</th>
                        <th>Passed</th>
                        <th>Failed</th>
                        <th>Skipped</th>
                    </tr>

                    <tr>
                        <td><b>${env.TOTAL}</b></td>
                        <td style="color:#2ecc71;"><b>${env.PASSED}</b></td>
                        <td style="color:#e74c3c;"><b>${env.FAILED}</b></td>
                        <td style="color:#f1c40f;"><b>${env.SKIPPED}</b></td>
                    </tr>

                </table>

                <br><br>

                <table border="0" cellpadding="6">
                    <tr>
                        <td><b>Job Name:</b></td>
                        <td>${env.JOB_NAME}</td>
                    </tr>
                    <tr>
                        <td><b>Build Number:</b></td>
                        <td>${env.BUILD_NUMBER}</td>
                    </tr>
                    <tr>
                        <td><b>Status:</b></td>
                        <td><b>${currentBuild.currentResult}</b></td>
                    </tr>
                    <tr>
                        <td><b>Execution Time:</b></td>
                        <td>${currentBuild.durationString}</td>
                    </tr>
                </table>

                <br><br>

                <a href="${env.BUILD_URL}"
                   style="background:#3498db;color:white;padding:10px 15px;text-decoration:none;border-radius:5px;">
                   🔍 View Build
                </a>

                &nbsp;&nbsp;

                <a href="${env.BUILD_URL}artifact/target/"
                   style="background:#27ae60;color:white;padding:10px 15px;text-decoration:none;border-radius:5px;">
                   📊 View Reports
                </a>

                <hr>

                <p style="font-size:12px;color:gray;">
                    This is an automated email from Jenkins CI pipeline.
                </p>

                </body>
                </html>
                """,

                mimeType: 'text/html',

                attachmentsPattern: 'target/*ExtentReport*.html'
            )
        }
    }
}