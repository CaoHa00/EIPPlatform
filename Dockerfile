# # Stage 1: Build
# FROM maven:3.9.9-eclipse-temurin-17 AS builder
# WORKDIR /app
# COPY . .
# RUN mvn clean package 

# # Stage 2: Runtime
# FROM eclipse-temurin:17-jdk-alpine
# WORKDIR /app
# COPY --from=builder /app/target/EIPplatform-0.0.1-SNAPSHOT.jar app.jar

# EXPOSE 7000
# ENTRYPOINT ["java","-jar","app.jar"]


FROM eclipse-temurin:17-jdk-alpine

# Install dependencies
# RUN apk add --no-cache \
#         tzdata \
#         curl \
#         ca-certificates \
#         fontconfig \
#         ttf-dejavu \
#     && curl -Lo /tmp/speedtest.tgz https://install.speedtest.net/app/cli/ookla-speedtest-1.2.0-linux-x86_64.tgz \
#     && tar -xzf /tmp/speedtest.tgz -C /usr/local/bin \
#     && rm -f /tmp/speedtest.tgz \
#     && chmod +x /usr/local/bin/speedtest

# Set timezone
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Create app directory
WORKDIR /app

# Copy JAR file
COPY target/EIPplatform-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 7000

# JVM options
ENV JAVA_TOOL_OPTIONS="-Djava.awt.headless=true -XX:MaxRAMPercentage=75.0"

# Run application
ENTRYPOINT ["java", "-Duser.timezone=Asia/Ho_Chi_Minh", "-jar", "app.jar"]