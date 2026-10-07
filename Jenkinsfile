pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-21'
    }

    environment {
        APP_NAME = 'server-patch-dashboard'
        JAR_NAME = 'dashboard-0.0.1-SNAPSHOT.jar'
    }

    stages {
        // =====================================================
        //  CI — Build & Unit Test  (Weeks 7-8)
        // =====================================================
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build/Compile') {
            steps {
                dir('backend') {
                    bat 'mvn clean compile'
                }
            }
        }

        stage('Run Tests') {
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

        stage('Package') {
            steps {
                dir('backend') {
                    bat 'mvn package -DskipTests'
                }
            }
        }

        stage('Archive JAR') {
            steps {
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
            }
        }

        // =====================================================
        //  Deploy JAR locally  (Week 8)
        // =====================================================
        stage('Deploy JAR') {
            steps {
                // Stop any previous instance on port 8080
                bat '''
                    FOR /F "tokens=5" %%P IN ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') DO (
                        taskkill /PID %%P /F 2>nul
                    )
                    exit /b 0
                '''
                // Start new instance in background
                bat '''
                    set DB_HOST=%DB_HOST%
                    set DB_PORT=%DB_PORT%
                    set DB_NAME=%DB_NAME%
                    set DB_USERNAME=%DB_USERNAME%
                    set DB_PASSWORD=%DB_PASSWORD%
                    start /B java -jar backend\\target\\%JAR_NAME% > backend_app.log 2>&1
                '''
                // Wait for startup
                bat '''
                    ping -n 20 127.0.0.1 > nul
                '''
                // Verify the backend responds
                bat '''
                    curl -s -o nul -w "%%{http_code}" http://localhost:8080/api/dashboard/summary | findstr "200"
                '''
            }
        }

        // =====================================================
        //  Selenium Tests  (Weeks 9-10)
        // =====================================================
        stage('Selenium Tests') {
            steps {
                bat '''
                    pip install -r tests\\selenium\\requirements.txt
                    set BACKEND_URL=http://localhost:8080
                    python -m pytest tests\\selenium\\test_dashboard.py::TestBackendAPI -v --junitxml=tests\\selenium\\results.xml
                '''
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'tests/selenium/results.xml'
                    // Stop the JAR process after Selenium tests
                    bat '''
                        FOR /F "tokens=5" %%P IN ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') DO (
                            taskkill /PID %%P /F 2>nul
                        )
                        exit /b 0
                    '''
                }
            }
        }

        // =====================================================
        //  Docker CD  (Weeks 11-12)
        // =====================================================
        stage('Docker Build & Deploy') {
            steps {
                // Stop and remove old containers
                bat '''
                    docker compose down --remove-orphans 2>nul
                    exit /b 0
                '''
                // Build images and start containers
                bat 'docker compose up -d --build'
                // Wait for backend container to be healthy
                bat '''
                    ping -n 30 127.0.0.1 > nul
                '''
                // Verify the deployed application
                bat '''
                    curl -s -o nul -w "%%{http_code}" http://localhost:8080/api/dashboard/summary | findstr "200"
                '''
            }
        }
    }

    post {
        success {
            echo "Pipeline succeeded for ${APP_NAME}"
        }
        failure {
            echo "Pipeline failed for ${APP_NAME}"
        }
    }
}
