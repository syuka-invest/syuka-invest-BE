// /backend/Jenkinsfile

pipeline {
    agent any

    // --- 환경 변수 설정 ---
    environment {
        DOCKER_HUB_CREDENTIALS_ID = 'syuka-invest-backend-dockerhub' 
        DOCKER_IMAGE_NAME = 'blueconecell/syukainvest-backend'
    }

    stages {
        // --- 1. 소스코드 가져오기 ---
        stage('Checkout') {
            steps {
                // 이전 빌드에서 남은 파일이 없도록 작업 공간을 깨끗이 정리합니다.
                cleanWs()
                // Jenkins Job 설정에 명시된 Git 레포지토리의 소스코드를 가져옵니다.
                checkout scm
            }
        }

        // --- 2. Gradle 빌드 ---
        stage('Gradle Build') {
            steps {
                // 경로 수정: backend/Syuka-invest 로 변경
                dir('backend/Syuka-invest') {
                    echo 'Gradle 빌드를 시작합니다.'
                    sh 'chmod +x gradlew'
                    sh './gradlew build'
                }
            }
        }

        // --- 3. Docker 이미지 빌드 및 푸시 ---
        stage('Build and Push Docker Image') {
            steps {
                // 경로 수정: backend/Syuka-invest 로 변경
                dir('backend/Syuka-invest') {
                    withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        script {
                            echo "Docker 이미지를 빌드하고 푸시합니다."
                            def imageTag = "${env.BUILD_NUMBER}"
                            
                            sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                            sh "docker build -t ${DOCKER_IMAGE_NAME}:${imageTag} ."
                            sh "docker tag ${DOCKER_IMAGE_NAME}:${imageTag} ${DOCKER_IMAGE_NAME}:latest"
                            sh "docker push ${DOCKER_IMAGE_NAME}:${imageTag}"
                            sh "docker push ${DOCKER_IMAGE_NAME}:latest"
                        }
                    }
                }
            }
        }

        // --- 4. Docker Compose로 배포 ---
        stage('Deploy') {
            steps {
                // 경로 수정: backend/Syuka-invest 로 변경
                dir('backend/Syuka-invest') {
                    echo 'Docker Compose로 배포를 시작합니다.'
                    sh "docker-compose -f docker-compose-infra.yml -f docker-compose-backend.yml pull backend"
                    sh "docker-compose -f docker-compose-infra.yml -f docker-compose-backend.yml up -d"
                }
            }
        }
    }

    // --- 빌드 후 정리 작업 ---
    post {
        always {
            echo '파이프라인이 종료되었습니다. Docker 로그아웃을 실행합니다.'
            sh 'docker logout'
        }
    }
}