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
                bat 'mvn clean test'   // use sh 'mvn clean test' for Linux
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/**/*.json', allowEmptyArchive: true
        }
    }
}