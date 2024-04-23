pipeline {
    agent any

    environment {
        REMOTE_HOST = credentials('REMOTE_HOST')
        REMOTE_USER = credentials('REMOTE_USER')
    }

    stages {
        stage('Deploy') {
            steps {
                script {
                    // 배포 서버로 .env 파일 전송
                    sh "scp .env ${REMOTE_USER}@${REMOTE_HOST}:~/.env"

                    // 배포 서버에서 Docker 이미지 빌드 및 실행
                    sshagent(credentials: ['your_ssh_credentials_id']) {
                        sh "ssh ${REMOTE_USER}@${REMOTE_HOST} 'sudo docker build -t ddps-spring-app .'"
                        sh "ssh ${REMOTE_USER}@${REMOTE_HOST} 'sudo docker run -d --name spring-app -p 8989:8989 --env-file /home/your_remote_user/.env ddps-spring-app'"
                    }
                }
            }
        }
    }
}
