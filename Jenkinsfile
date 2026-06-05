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
                        def cleanTag = tag.replace('@','')

                        jobs["Run ${tag}"] = {
                            echo "Running for tag: ${tag}"

                            bat "mvn clean test -Dcucumber.filter.tags=\"${tag}\" -Dreport.name=${cleanTag} -Dcucumber.plugin=json:target/jsonReports/${cleanTag}.json"
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

                    // DEBUG: list files
                    bat "dir target\\jsonReports"

                    tags.each { tag ->

                        def cleanTag = tag.replace('@','')
                        def filePath = "target/jsonReports/${cleanTag}.json"

                        echo "Looking for file: ${filePath}"

                        if (!fileExists(filePath)) {
                            echo "WARNING: File not found -> ${filePath}"
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
                        <td><b>${env.TOTAL ?: '0'}</b></td>
                        <td style="color:#2ecc71;"><b>${env.PASSED ?: '0'}</b></td>
                        <td style="color:#e74c3c;"><b>${env.FAILED ?: '0'}</b></td>
                        <td style="color:#f1c40f;"><b>${env.SKIPPED ?: '0'}</b></td>
                    </tr>

                </table>

                <br><br>

                <a href="${env.BUILD_URL}artifact/target/"
                   style="background:#27ae60;color:white;padding:10px 15px;text-decoration:none;border-radius:5px;">
                   📊 View Reports
                </a>

                </body>
                </html>
                """,

                mimeType: 'text/html',

                attachmentsPattern: 'target/*ExtentReport*.html'
            )
        }
    }
}
