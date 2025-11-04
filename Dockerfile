# Use OpenJDK base image
FROM eclipse-temurin:17-jdk-alpine

# Install tzdata and speedtest CLI
WORKDIR /app

# Copy your JAR file into the image
COPY target/EIPplatform-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 7000

# Set the container OS timezone
ENV TZ=Asia/Ho_Chi_Minh

# Force JVM timezone
ENTRYPOINT ["sh", "-c", "java -Duser.timezone=Asia/Ho_Chi_Minh $JAVA_OPTS -jar /app.jar"]