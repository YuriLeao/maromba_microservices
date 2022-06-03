FROM amazoncorretto:17.0.3
WORKDIR /app
COPY gateway-server/target/*.jar /app/gateway-server.jar
RUN bash -c 'touch /app/gateway-server.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:dev/./urandom", "-jar", "gateway-server.jar"]