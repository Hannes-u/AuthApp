FROM openjdk:11
COPY target/AuthApp-0.0.1-SNAPSHOT.jar AuthApp.jar
ENTRYPOINT ["java","-jar","/AuthApp.jar"]