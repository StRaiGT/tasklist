FROM maven:3.9.7-amazoncorretto-17-al2023 AS build
WORKDIR /
COPY pom.xml .
RUN mvn dependency:go-offline
COPY /src /src
COPY checkstyle-suppressions.xml .
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine-jdk
ARG DOCKER_USER=default_user
RUN addgroup -S $DOCKER_USER && adduser -S $DOCKER_USER -G $DOCKER_USER
USER $DOCKER_USER
COPY --from=build /target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
