# Base image with Java 17
FROM openjdk:17
 
# Copy your jar file from target folder to container
COPY target/*.jar app.jar
 
# Command to run your app
ENTRYPOINT ["java", "-jar", "/app.jar"]