FROM eclipse-temurin:21-jdk-alpine-3.20 AS builder
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests || true
FROM eclipse-temurin:21-jre-alpine-3.20 AS runtime
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8083
#ENV SPRING_PROFILES_ACTIVE=prod
HEALTHCHECK --interval=30s --timeout=5s CMD curl -f http://localhost:8083/actuator/health || exit 1
CMD ["java", "-jar", "app.jar"]