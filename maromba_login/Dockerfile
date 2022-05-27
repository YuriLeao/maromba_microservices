FROM amazoncorretto:11.0.9

WORKDIR /app

COPY target/maromba-0.0.1-SNAPSHOT.jar /app/maromba-api.jar

ENTRYPOINT ["java", "-jar", "maromba-api.jar"]