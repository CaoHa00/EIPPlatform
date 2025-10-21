# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Install tzdata and speedtest CLI
RUN apt-get update && \
    apt-get install -y --no-install-recommends tzdata curl gnupg && \
    curl -s https://packagecloud.io/install/repositories/ookla/speedtest-cli/script.deb.sh | bash && \
    apt-get install -y speedtest && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy your JAR file into the image
COPY target/SmartBuildingBackend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9094

# Set the container OS timezone
ENV TZ=Asia/Ho_Chi_Minh

# Force JVM timezone
ENTRYPOINT ["sh", "-c", "java -Duser.timezone=Asia/Ho_Chi_Minh $JAVA_OPTS -jar /app.jar"]