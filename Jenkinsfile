pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_VERSION = "3.8"
        COMPOSE_FILE = "docker-compose.yml"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'jenkins-doc-git', url: 'https://github.com/elowill666/GameRoot.git'
            }
        }
        stage('Build') {
            steps {
                script {
                    // 設定 Docker 的 PATH 路徑
                    sh 'export PATH=$PATH:/usr/local/bin && docker --version && docker compose version'
                }
            }
        }
        stage('Up Docker Containers') {
            steps {
                script {
                    sh 'export PATH=$PATH:/usr/local/bin && docker compose -f ${COMPOSE_FILE} up -d --build'
                }
            }
        }

        stage('Tear Down Docker Containers') {
            steps {
                script {
                    sh 'export PATH=$PATH:/usr/local/bin && docker compose -f ${COMPOSE_FILE} down'
                }
            }
        }
    }

    post {
        always {
            sh 'export PATH=$PATH:/usr/local/bin && docker compose -f ${COMPOSE_FILE} down'
        }
    }
}
