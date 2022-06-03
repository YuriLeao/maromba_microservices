FROM amazoncorretto:17.0.3
WORKDIR /app
COPY usuario-service/target/*.jar /app/usuario-service.jar
RUN bash -c 'touch /app/usuario-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "usuario-service.jar"]