FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install -y nmon htop openjdk-8-jdk maven phalanx gnuchess
RUN update-ca-certificates -f
RUN ln -s /usr/games/gnuchess /usr/bin/gnuchess
RUN ln -s /usr/games/phalanx /usr/bin/phalanx

COPY target/*.jar /usr/local/chesscorp/

EXPOSE 8080
VOLUME /data

CMD java -Dspring.datasource.url=jdbc:h2:file:/data/chess1	\
	 -Dspring.jpa.database=H2				\
	 -jar /usr/local/chesscorp/*.jar
