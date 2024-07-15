FROM amazoncorretto:17.0.3
WORKDIR /app
COPY exercise-service/target/*.jar /app/exercise-service.jar
RUN bash -c 'touch /app/exercise-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "exercise-service.jar"]