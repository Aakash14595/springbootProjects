
pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                bat '''
                    @echo off
                    set "DEPLOY_DIR=C:\\SpringBootDeploy\\userjpa"

                    echo Stopping Spring Boot service...
                    sc stop UserJpaService

                    echo Waiting for service to stop...
                    set /a COUNT=0

                    :WAIT_STOP
                    sc query UserJpaService | findstr /C:"STOPPED" >nul
                    if not errorlevel 1 goto COPY_JAR

                    timeout /t 2 /nobreak >nul
                    set /a COUNT+=1
                    if %COUNT% GEQ 30 exit /b 1
                    goto WAIT_STOP

                    :COPY_JAR
                    echo Copying new JAR...
                    copy /Y "target\\userjpa-0.0.1-SNAPSHOT.jar" "%DEPLOY_DIR%\\userjpa.jar"
                    if errorlevel 1 exit /b 1

                    echo Starting Spring Boot service...
                    sc start UserJpaService
                    if errorlevel 1 exit /b 1

                    echo Deployment command completed.
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                bat '''
                    @echo off
                    set /a COUNT=0

                    :CHECK_API
                    curl --silent --fail --max-time 5 -o NUL http://localhost:9090/api/users
                    if not errorlevel 1 exit /b 0

                    timeout /t 5 /nobreak >nul
                    set /a COUNT+=1
                    if %COUNT% GEQ 12 exit /b 1
                    goto CHECK_API
                '''
            }
        }
    }

    post {
        success {
            echo 'Spring Boot deployment successful!'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }

        failure {
            echo 'Pipeline failed. Check Jenkins console output.'
        }
    }
}
