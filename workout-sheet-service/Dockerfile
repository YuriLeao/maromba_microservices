FROM amazoncorretto:17.0.3
WORKDIR /app
COPY workout-sheet-service/target/*.jar /app/workout-sheet-service.jar
RUN bash -c 'touch /app/workout-sheet-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "workout-sheet-service.jar"]