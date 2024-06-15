FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
RUN mkdir /upload-dir
COPY target/BE-v1.jar app.jar
COPY target/classes/application.properties application.properties
ENTRYPOINT ["java", "-jar", "/app.jar"]