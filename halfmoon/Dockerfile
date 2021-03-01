FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/halfmoon.jar halfmoon.jar
ENTRYPOINT ["java","-jar","/halfmoon.jar", "&"]