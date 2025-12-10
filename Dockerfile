FROM maven:3.9.11-eclipse-temurin-25 AS build

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY . .
RUN apt-get update \
    && apt install curl -y  \
    && curl -sL https://deb.nodesource.com/setup_22.x | bash -  \
    && apt-get install nodejs -y
RUN mvn clean package


FROM eclipse-temurin:25

COPY --from=build /usr/src/app/target/saber.jar .

EXPOSE 8888

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "saber.jar"]
