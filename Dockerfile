#FROM maven:3.6.1-jdk-8-alpine AS build
# https://github.com/nekolr/maven-image/tree/master/3.6.1-jdk-8
FROM nekolr/maven:3.6.1 AS build

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY . .

ARG NODE_VERSION=v14.17.0
RUN apt install wget -y && wget https://nodejs.org/dist/$NODE_VERSION/node-$NODE_VERSION-linux-x64.tar.xz && tar -xf node-$NODE_VERSION-linux-x64.tar.xz
RUN ln -s /usr/src/app/node-$NODE_VERSION-linux-x64/bin/node /usr/local/bin/node
RUN ln -s /usr/src/app/node-$NODE_VERSION-linux-x64/bin/npm /usr/local/bin/npm

RUN mvn clean package


FROM openjdk:8-jdk-alpine

COPY --from=build /usr/src/app/target/saber.jar .

EXPOSE 8888

CMD java -jar saber.jar
