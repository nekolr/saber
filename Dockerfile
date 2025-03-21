FROM maven:3.9.6-eclipse-temurin-21 AS build

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY . .
RUN apt-get update \
    && apt install curl -y  \
    && curl -sL https://deb.nodesource.com/setup_20.x | bash -  \
    && apt-get install nodejs -y
RUN mvn clean package


FROM openjdk:21-slim

COPY --from=build /usr/src/app/target/saber.jar .

EXPOSE 8888

CMD java -Djava.security.egd=file:/dev/./urandom -jar saber.jar
