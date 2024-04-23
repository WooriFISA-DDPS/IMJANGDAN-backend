pipeline {
    agent any

    environment {
        GRADLE_HOME = tool 'Gradle'
        PATH = "$GRADLE_HOME/bin:$PATH"
    }

    stages {
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
        }

        stage('Test') {
            steps {
                sh 'gradle test'
            }
        }

        stage('Deploy') {
            steps {
                // 예시: Docker 이미지 빌드 및 실행
                sh 'docker build -t ddps-app .'
                sh 'docker run -d --name ddps-back -p 8989:8989 ddps-app'
            }
        }
    }
}
