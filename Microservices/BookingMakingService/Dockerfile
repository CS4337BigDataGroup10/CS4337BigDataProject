FROM openjdk:23-jdk-slim

# Expose the debug port (5005) and application port (e.g., 8080)
EXPOSE 5005 8080

# Copy the Spring Boot JAR file into the container
COPY build/libs/BookingMakingService-0.0.1-SNAPSHOT-plain.jar app.jar

# Set the entry point to include debugging capabilities
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]
