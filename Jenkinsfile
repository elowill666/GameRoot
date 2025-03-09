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

        stage('Build Docker Images') {
            steps {
                script {
                    sh 'curl -L https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose'
                    sh 'chmod +x /usr/local/bin/docker-compose'

                    sh 'docker-compose -f ${COMPOSE_FILE} build'
                }
            }
        }

        stage('Up Docker Containers') {
            steps {
                script {
                    sh 'docker-compose -f ${COMPOSE_FILE} up -d'
                }
            }
        }

        stage('Tear Down Docker Containers') {
            steps {
                script {
                    sh 'docker-compose -f ${COMPOSE_FILE} down'
                }
            }
        }
    }

    post {
        always {
            sh 'docker-compose -f ${COMPOSE_FILE} down'
        }
    }
}
