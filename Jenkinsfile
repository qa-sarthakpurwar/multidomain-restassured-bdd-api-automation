pipeline {
    agent any

    parameters {
        string(name: 'RUN', defaultValue: '@ECommerce,@GooglePlace', description: 'Tags')
    }

    stages {

        stage('Checkout') {
            steps {
                deleteDir()
                checkout scm
            }
        }

        stage('Run Tests in Parallel') {
            steps {
                script {

                    def tags = params.RUN.split(",")
                    def jobs = [:]

                    tags.each { tag ->

                        def cleanTag = tag.trim().replace('@','')

                        jobs["Run ${cleanTag}"] = {

                            dir("run-${cleanTag}") {

                                checkout scm

                                echo "Running tag: ${tag}"

                                bat "mvn clean test -Dcucumber.filter.tags=\"${tag}\" -Dreport.name=${cleanTag}"

                                bat """
                                if exist target\\ExtentReport.html (
                                    rename target\\ExtentReport.html ExtentReport-${cleanTag}.html
                                    echo Renamed report for ${cleanTag}
                                ) else (
                                    echo WARNING: ExtentReport missing for ${cleanTag}
                                )
                                """
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

                        def cleanTag = tag.trim().replace('@','')
                        def filePath = "run-${cleanTag}/target/jsonReports/cucumber.json"

                        if (fileExists(filePath)) {

                            def json = readFile(filePath)
                            def report = new groovy.json.JsonSlurper().parseText(json)

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

                        } else {
                            echo "Missing report: ${filePath}"
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

            archiveArtifacts artifacts: '**/target/**/*.*', allowEmptyArchive: true

            emailext(
                to: 'qa.sarthakpurwar@gmail.com',
                subject: "Build #${env.BUILD_NUMBER} | ${currentBuild.currentResult}",

                body: """
                <html>
                <body>

                <h2>Parallel Execution Summary</h2>

                <p><b>Executed Tags:</b> ${params.RUN}</p>

                <table border="1" cellpadding="8">
                    <tr bgcolor="#2c3e50">
                        <th><font color="white">Total</font></th>
                        <th><font color="white">Passed</font></th>
                        <th><font color="white">Failed</font></th>
                        <th><font color="white">Skipped</font></th>
                    </tr>
                    <tr>
                        <td>${env.TOTAL ?: '0'}</td>
                        <td><font color="green">${env.PASSED ?: '0'}</font></td>
                        <td><font color="red">${env.FAILED ?: '0'}</font></td>
                        <td><font color="orange">${env.SKIPPED ?: '0'}</font></td>
                    </tr>
                </table>

                </body>
                </html>
                """,

                mimeType: 'text/html',
                attachmentsPattern: '**/target/ExtentReport-*.html'
            )
        }
    }
}