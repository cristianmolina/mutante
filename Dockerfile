FROM openjdk:17-alpine

ENV spring.redis.host ${REDIS_HOST}
ENV spring.redis.port ${REDIS_PORT}

ADD target/mutante-0.0.1-SNAPSHOT.jar /usr/share/app.jar

ENTRYPOINT ["java", "-jar", "/usr/share/app.jar"]
