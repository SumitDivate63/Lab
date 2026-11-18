pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Build Docker image') {
            steps {
                sh 'docker build -t simple-java-webapp:${BUILD_NUMBER} .'
            }
        }
    }
}
