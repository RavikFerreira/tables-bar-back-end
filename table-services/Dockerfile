kerfile
#
# Build stage
#
FROM maven:3.8.3-openjdk-21 AS build
COPY . .
RUN mvn clean install

#
# Package stage
#
FROM eclipse-temurin:21-jdk
COPY --from=build /target/your-build.jar app.jar
# ENV PORT=8080
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.jar"]