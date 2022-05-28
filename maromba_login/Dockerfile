FROM amazoncorretto:11.0.9

WORKDIR /app

COPY target/maromba_login-0.0.1-SNAPSHOT.jar /app/maromba-login.jar

ENTRYPOINT ["java", "-jar", "maromba-login.jar"]