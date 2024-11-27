# Base image
FROM openjdk:23-jdk-slim

# Copy across build
COPY build/libs/TourManagementService-0.0.1-SNAPSHOT.jar plain.jar

# Set entry point
ENTRYPOINT [ "java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "plain.jar" ]
