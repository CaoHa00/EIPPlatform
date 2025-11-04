# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/EIPplatform-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 7000
ENTRYPOINT ["java","-jar","app.jar"]