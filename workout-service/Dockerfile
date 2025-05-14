FROM amazoncorretto:17.0.3
WORKDIR /app
COPY executed-workout-service/target/*.jar /app/executed-workout-service.jar
RUN bash -c 'touch /app/executed-workout-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "executed-workout-service.jar"]