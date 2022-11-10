FROM amazoncorretto:17.0.3
WORKDIR /app
COPY exercicio-service/target/*.jar /app/exercicio-service.jar
RUN bash -c 'touch /app/exercicio-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "exercicio-service.jar"]