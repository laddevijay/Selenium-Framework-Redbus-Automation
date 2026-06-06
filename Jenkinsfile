pipeline {
    agent any

    tools {
        maven 'Maven'   // ensure configured in Jenkins
        jdk 'JDK17'
    }

    stages {

        stage('Checkout') {
            steps {
                git url: '<YOUR_GIT_REPO_URL>', branch: 'main'
            }
        }

        stage('Run Tests') {
            steps {
                // Use 'bat' if you're on Windows
                bat 'mvn clean test'
                // sh 'mvn clean test'  // use this for Linux/Mac
            }
        }

        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'reports/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'test-output/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'logs/**', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            echo 'Execution completed'
        }
    }
}