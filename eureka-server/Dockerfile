FROM amazoncorretto:17.0.3
WORKDIR /app
COPY eureka-server/target/*.jar /app/eureka-server.jar
RUN bash -c 'touch /app/eureka-server.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "eureka-server.jar"]