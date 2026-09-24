FROM maven:3.9.16-eclipse-temurin-21 AS build
WORKDIR /twitter-api-project
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTest

FROM eclipse-temurin:21-jre
COPY --from=build /twitter-api-project/target/*.jar /twitter-api-project.jar
ENTRYPOINT ["java", "-jar", "/twitter-api-project.jar"]