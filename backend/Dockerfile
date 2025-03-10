# 1. 建置階段（Build Stage）
FROM eclipse-temurin:21-jdk AS builder

# 安裝必要的工具
RUN apt-get update \
    && apt-get install -y ca-certificates curl git openssh-client --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

# 下載並安裝 Maven
LABEL org.opencontainers.image.title="Apache Maven"
LABEL org.opencontainers.image.source="https://github.com/carlossg/docker-maven"
LABEL org.opencontainers.image.url="https://github.com/carlossg/docker-maven"
LABEL org.opencontainers.image.description="Apache Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information."

ENV MAVEN_HOME=/usr/share/maven
COPY --from=maven:3.9.9-eclipse-temurin-17 ${MAVEN_HOME} ${MAVEN_HOME}
COPY --from=maven:3.9.9-eclipse-temurin-17 /usr/local/bin/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY --from=maven:3.9.9-eclipse-temurin-17 /usr/share/maven/ref/settings-docker.xml /usr/share/maven/ref/settings-docker.xml

RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

ARG MAVEN_VERSION=3.9.9
ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG="$USER_HOME_DIR/.m2"

WORKDIR /app
COPY . /app
# 這裡運行 Maven 构建过程
RUN mvn clean package -DskipTests

# 2. 運行階段（Run Stage）
FROM openjdk:21-buster

WORKDIR /app

# 從構建階段（builder）複製結果 JAR 文件
COPY --from=builder /app/target/finalproject-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

# 運行容器時執行 JAR 文件
CMD ["java", "-jar", "app.jar"]
