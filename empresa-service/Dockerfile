FROM amazoncorretto:17.0.3
WORKDIR /app
COPY empresa-service/target/*.jar /app/empresa-service.jar
RUN bash -c 'touch /app/empresa-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "empresa-service.jar"]