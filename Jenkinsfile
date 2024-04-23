pipeline {
    agent {
        label '!windows'
    }

    environment {
        DISABLE_AUTH = 'true'
        DB_ENGINE    = 'sqlite'
    }

    stages {
        stage('Debug') {
            steps {
                echo "Running on agent: ${env.NODE_NAME}"
                echo "Database engine is ${DB_ENGINE}"
                echo "DISABLE_AUTH is ${DISABLE_AUTH}"
                sh 'printenv'
            }
        }

        stage('Build') {
            steps {
                echo "Executing Build Stage"
                // 빌드 스텝 추가
            }
        }
    }
}
