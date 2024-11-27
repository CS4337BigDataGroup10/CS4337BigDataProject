# Use an OpenJDK base image
FROM openjdk:17-jdk-slim


# Copy the Spring Boot application JAR to the container
COPY build/libs/EurekaServer-0.0.1-SNAPSHOT.jar plain.jar


# Run the Eureka Server
ENTRYPOINT ["java", "-jar", "plain.jar"]
