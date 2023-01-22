FROM openjdk:17
COPY target/*.jar /opt/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]