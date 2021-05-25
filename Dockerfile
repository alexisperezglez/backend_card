# Stage 1, based on Nginx, to have only the compiled app, ready for production with Nginx
FROM openjdk:11.0.11-jdk as build-stage
COPY target/ /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]