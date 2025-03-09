FROM openjdk:latest

# 設定工作目錄為/app
WORKDIR /app

# 將本機目錄下的Spring Boot應用程式可執行JAR複製到容器中
COPY target/finalproject-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8081

# 定義ENTRYPOINT指令以運行Spring Boot應用程式
# ENTRYPOINT ["java", "-jar", "app.jar"]

# 或者只使用CMD
CMD ["java", "-jar", "app.jar"]
