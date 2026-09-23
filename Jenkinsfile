pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-21'
    }

    environment {
        APP_NAME = 'server-patch-dashboard'
        ARTIFACT_DIR = 'backend/target'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend - Compile') {
            steps {
                dir('backend') {
                    bat 'mvn clean compile -DskipTests'
                }
            }
        }

        stage('Backend - Unit Tests') {
            steps {
                dir('backend') {
                    bat 'mvn test'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Backend - Package') {
            steps {
                dir('backend') {
                    bat 'mvn package -DskipTests'
                }
            }
        }

        stage('Frontend - Install') {
            steps {
                dir('frontend') {
                    bat 'npm ci'
                }
            }
        }

        stage('Frontend - Build') {
            steps {
                dir('frontend') {
                    bat 'npm run build'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
                archiveArtifacts artifacts: 'frontend/dist/**', allowEmptyArchive: true
            }
        }
    }

    post {
        success {
            echo "Build succeeded for ${APP_NAME}"
        }
        failure {
            echo "Build failed for ${APP_NAME}"
        }
        always {
            cleanWs()
        }
    }
}
