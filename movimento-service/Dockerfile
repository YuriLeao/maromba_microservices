FROM amazoncorretto:17.0.3
WORKDIR /app
COPY movimento-service/target/*.jar /app/movimento-service.jar
RUN bash -c 'touch /app/movimento-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "movimento-service.jar"]