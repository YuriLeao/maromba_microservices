FROM amazoncorretto:17.0.3
WORKDIR /app
COPY grupoMuscular-service/target/*.jar /app/grupoMuscular-service.jar
RUN bash -c 'touch /app/grupoMuscular-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "grupoMuscular-service.jar"]