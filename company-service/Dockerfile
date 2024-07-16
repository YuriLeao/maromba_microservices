FROM amazoncorretto:17.0.3
WORKDIR /app
COPY company-service/target/*.jar /app/company-service.jar
RUN bash -c 'touch /app/company-service.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "company-service.jar"]