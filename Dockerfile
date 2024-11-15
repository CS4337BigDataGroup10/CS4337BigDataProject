# Build Stage
FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY . /app
RUN ./gradlew clean build  # Or mvn clean package

# Runtime Stage
FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/CS4337BigDataProject-1.0.0-SNAPSHOT.jar app.jar

# Set entry point (with optional debugging)
ENTRYPOINT ["java", "-jar", "app.jar"]
