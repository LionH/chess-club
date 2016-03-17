FROM ubuntu:15.10

RUN apt-get update
RUN apt-get install -y nmon htop openjdk-8-jdk maven phalanx gnuchess
RUN update-ca-certificates -f
RUN ln -s /usr/games/gnuchess /usr/bin/gnuchess
RUN ln -s /usr/games/phalanx /usr/bin/phalanx

COPY target/*.jar /usr/local/chesscorp/

EXPOSE 8080

CMD java -jar /usr/local/chesscorp/*.jar
