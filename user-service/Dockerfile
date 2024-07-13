FROM amazoncorretto:17.0.3
WORKDIR /app
COPY user-service/target/*.jar /app/user-service.jar
RUN bash -c 'touch /app/user-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "user-service.jar"]