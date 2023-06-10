FROM alpine

MAINTAINER Niklas Feuerstein, <contact@feuerstein.dev>

RUN apk add --no-cache openjdk17-jre-headless

RUN adduser -D -h /home/kottbot/ kottbot
ENV  USER=kottbot HOME=/home/kottbot
WORKDIR /home/container

ADD kottbot.jar

CMD ["/bin/sh/", "java", "-jar", "kottbot.jar"]
