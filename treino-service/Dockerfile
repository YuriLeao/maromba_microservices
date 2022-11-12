FROM amazoncorretto:17.0.3
WORKDIR /app
COPY treino-service/target/*.jar /app/treino-service.jar
RUN bash -c 'touch /app/treino-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "treino-service.jar"]