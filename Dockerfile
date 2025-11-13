
FROM eclipse-temurin:17-jdk-alpine


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