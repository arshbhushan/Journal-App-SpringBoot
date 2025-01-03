# Use a Maven image to build the application
FROM maven:3.9.5-eclipse-temurin-17 as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the source code into the container
COPY . .

# Build the application using Maven
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the application
FROM eclipse-temurin:17-jre-alpine

# Set environment variables (values will be provided during deployment)
ENV SERVER_PORT=8080 \
    MONGODB_HOST=mongodb://example.mongodb.net \
    REDIS_HOST=example.redis.com \
    REDIS_PASSWORD=securepassword \
    JAVA_EMAIL=example@example.com \
    JAVA_EMAIL_PASSWORD=securepassword \
    KAFKA_SERVERS=localhost:9092 \
    CLIENT_ID=journal-app-client \
    GOOGLE_CLIENT_ID=your-google-client-id \
    GOOGLE_CLIENT_SECRET=your-google-client-secret

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
