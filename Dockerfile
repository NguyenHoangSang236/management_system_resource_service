FROM ubuntu:latest
FROM openjdk:19-jdk-alpine

COPY keystore.p12 keystore.p12
COPY target/ingredient-0.0.1-SNAPSHOT.jar ingredient-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/ingredient-0.0.1-SNAPSHOT.jar"]
EXPOSE 8082
