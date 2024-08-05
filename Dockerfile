FROM openjdk:21
WORKDIR /app
COPY target/*.jar /app/api.jar
EXPOSE 3001:3001
CMD ["java", "-jar", "api.jar"]