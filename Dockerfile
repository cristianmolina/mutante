FROM openjdk:17-alpine

ADD target/mutante-0.0.1-SNAPSHOT.jar /usr/share/app.jar

ENTRYPOINT ["java", "-jar", "/usr/share/app.jar"]
