FROM maven:3.8.1-openjdk-11-slim AS build

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY . .

RUN apt-get update && apt install curl -y && curl -sL https://deb.nodesource.com/setup_16.x | bash - && apt-get install nodejs -y

RUN mvn clean package


FROM openjdk:11.0.11-jre-slim

COPY --from=build /usr/src/app/target/saber.jar .

EXPOSE 8888

CMD java -Djava.security.egd=file:/dev/./urandom -jar saber.jar
