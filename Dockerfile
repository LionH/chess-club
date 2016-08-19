FROM ubuntu:16.04

RUN apt-get update && apt-get install -y nmon htop openjdk-8-jdk maven phalanx gnuchess
RUN update-ca-certificates -f
RUN ln -s /usr/games/gnuchess /usr/bin/gnuchess
RUN ln -s /usr/games/phalanx /usr/bin/phalanx

# Crafty is not included in the default linux distrib, so we comment it out for now
#RUN ln -s /usr/games/crafty /usr/bin/crafty

COPY . /usr/local/chesscorp/

RUN /usr/local/chesscorp/src/main/scripts/docker_build.sh

EXPOSE 8080
VOLUME /data

ENV dburl      jdbc:h2:file:/data/chess1
ENV dbdatabase H2
ENV dbddl      update

CMD java -Dspring.datasource.url=${dburl}		\
	 -Dspring.jpa.database=${dbdatabase}		\
	 -Dspring.jpa.hibernate.ddl-auto=${dbddl}	\
	 -jar /usr/local/chesscorp/target/*.jar
