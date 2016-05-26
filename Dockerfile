FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install -y nmon htop openjdk-8-jdk maven phalanx gnuchess crafty
RUN update-ca-certificates -f
RUN ln -s /usr/games/gnuchess /usr/bin/gnuchess
RUN ln -s /usr/games/phalanx /usr/bin/phalanx
RUN ln -s /usr/games/crafty /usr/bin/crafty

COPY . /usr/local/chesscorp/

RUN mvn -f /usr/local/chesscorp/pom.xml -DskipTests=true clean package

EXPOSE 8080
VOLUME /data

CMD java -Dspring.datasource.url=jdbc:h2:file:/data/chess1	\
	 -Dspring.jpa.database=H2				\
	 -Dspring.jpa.hibernate.ddl-auto=update			\
	 -jar /usr/local/chesscorp/target/*.jar
