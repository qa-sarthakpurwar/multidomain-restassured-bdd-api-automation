pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Parse Cucumber Report') {
            steps {
                script {

                    def filePath = 'target/jsonReports/cucumber-report.json'

                    def jsonText = readFile(filePath)
                    def report = new groovy.json.JsonSlurper().parseText(jsonText)

                    int total = 0
                    int passed = 0
                    int failed = 0
                    int skipped = 0

                    report.each { feature ->
                        feature.elements.each { scenario ->
                            total++

                            def statuses = scenario.steps.collect { it.result.status }

                            if (statuses.contains("failed")) {
                                failed++
                            } else if (statuses.contains("skipped")) {
                                skipped++
                            } else {
                                passed++
                            }
                        }
                    }

                    // Store values for email stage
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

            // Archive ALL reports
            archiveArtifacts artifacts: 'target/**/*.*', allowEmptyArchive: true

            // Send Email with attachment
            emailext(
                to: 'your-email@gmail.com',
                subject: "Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
                body: """
                    <h2>🚀 Automation Execution Summary</h2>

                    <table border="1" cellpadding="10" cellspacing="0">
                        <tr>
                            <th>Total</th>
                            <th>Passed</th>
                            <th>Failed</th>
                            <th>Skipped</th>
                        </tr>
                        <tr>
                            <td>${env.TOTAL}</td>
                            <td>${env.PASSED}</td>
                            <td>${env.FAILED}</td>
                            <td>${env.SKIPPED}</td>
                        </tr>
                    </table>

                    <br><br>

                    <b>Job:</b> ${env.JOB_NAME}<br>
                    <b>Build Number:</b> ${env.BUILD_NUMBER}<br>
                    <b>Status:</b> ${currentBuild.currentResult}<br>
                    <b>Execution Time:</b> ${currentBuild.durationString}<br>

                    <br>

                    <a href="${env.BUILD_URL}" style="background:#3498db;color:white;padding:10px 15px;text-decoration:none;">
                        View Build
                    </a>
                """,
                mimeType: 'text/html',

                // Attach HTML report
                attachmentsPattern: 'target/cucumber-report.html'
            )
        }
    }
}