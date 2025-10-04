# Stage 1: Build the application
FROM maven:3.9.8-amazoncorretto-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: need to update with a java 21 verision
FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/store-service.jar
ENTRYPOINT ["java", "-jar", "/app/store-service.jar"]
