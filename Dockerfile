FROM eclipse-temurin:17-jdk

EXPOSE 8887

ADD target/operations-service-0.0.1-SNAPSHOT.jar application.jar

ENTRYPOINT ["java", "-jar", "/application.jar"]
