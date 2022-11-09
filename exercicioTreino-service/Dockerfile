FROM amazoncorretto:17.0.3
WORKDIR /app
COPY exercicioTreino-service/target/*.jar /app/exercicioTreino-service.jar
RUN bash -c 'touch /app/exercicioTreino-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "exercicioTreino-service.jar"]