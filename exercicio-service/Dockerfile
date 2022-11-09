FROM amazoncorretto:17.0.3
WORKDIR /app
COPY exercicio_service/target/*.jar /app/exercicio_service.jar
RUN bash -c 'touch /app/exercicio_service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "exercicio_service.jar"]