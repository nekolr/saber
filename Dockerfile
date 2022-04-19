# https://github.com/nekolr/maven-image/tree/master/3.8.1-jdk-11-slim
FROM nekolr/maven:3.8.1-jdk-11-slim AS build

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY . .

ARG NODE_VERSION=v16.14.2
RUN apt-get update && apt install wget xz-utils -y && wget https://nodejs.org/download/release/$NODE_VERSION/node-$NODE_VERSION-linux-x64.tar.xz && tar -xf node-$NODE_VERSION-linux-x64.tar.xz
RUN ln -s /usr/src/app/node-$NODE_VERSION-linux-x64/bin/node /usr/local/bin/node
RUN ln -s /usr/src/app/node-$NODE_VERSION-linux-x64/bin/npm /usr/local/bin/npm

RUN mvn clean package


FROM openjdk:11.0.11-jre-slim

COPY --from=build /usr/src/app/target/saber.jar .

EXPOSE 8888

CMD java -Djava.security.egd=file:/dev/./urandom -jar saber.jar
