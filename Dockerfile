# https://github.com/nekolr/maven-image/tree/master/3.8.1-jdk-11-slim
FROM nekolr/maven:3.8.1-jdk-11-slim AS build

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY . .

RUN jlink --module-path jmods --add-modules java.base,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,jdk.unsupported --output jre

ARG NODE_VERSION=v14.17.0
RUN apt-get update && apt install wget xz-utils -y && wget https://nodejs.org/dist/$NODE_VERSION/node-$NODE_VERSION-linux-x64.tar.xz && tar -xf node-$NODE_VERSION-linux-x64.tar.xz
RUN ln -s /usr/src/app/node-$NODE_VERSION-linux-x64/bin/node /usr/local/bin/node
RUN ln -s /usr/src/app/node-$NODE_VERSION-linux-x64/bin/npm /usr/local/bin/npm

RUN mvn clean package


FROM debian:buster-slim

ENV JAVA_HOME=/usr/lib/jvm/jre
ENV PATH $JAVA_HOME/bin:$PATH

COPY --from=build /usr/src/app/jre/. /usr/lib/jvm/jre/
COPY --from=build /usr/src/app/target/saber.jar .

EXPOSE 8888

CMD java -jar saber.jar
