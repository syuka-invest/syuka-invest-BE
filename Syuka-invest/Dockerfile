FROM openjdk:24-jdk-slim

# Timezone 설정 (한국 시간)
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사 (Gradle 빌드 후 실행 가정)
# 통상적인 build/libs 경로의 jar 파일을 복사합니다.
COPY build/libs/*.jar app.jar

# 프로필 설정을 환경변수로 받아서 실행 (기본값 prod)
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]