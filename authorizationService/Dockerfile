FROM openjdk:12-jdk-alpine
VOLUME /tmp

COPY build/libs/*.jar app.jar
COPY src/main/resources/keys keys
ENTRYPOINT ["java","-cp","/keys" ,"-jar","/app.jar"]
