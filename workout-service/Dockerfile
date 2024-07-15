FROM amazoncorretto:17.0.3
WORKDIR /app
COPY workout-service/target/*.jar /app/workout-service.jar
RUN bash -c 'touch /app/workout-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "work-service.jar"]