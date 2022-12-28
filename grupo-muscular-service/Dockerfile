FROM amazoncorretto:17.0.3
WORKDIR /app
COPY grupo-muscular-service/target/*.jar /app/grupo-muscular-service.jar
RUN bash -c 'touch /app/grupo-muscular-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "grupo-muscular-service.jar"]