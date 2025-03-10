# 1. 建置階段（Build Stage）
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. 運行階段（Run Stage）
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/finalproject-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
