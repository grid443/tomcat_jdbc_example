FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/servlet-app-1.0-SNAPSHOT-jar-with-dependencies.jar

# cd /usr/local/runme
WORKDIR /usr/local/runme

# copy target/servlet-app-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/runme/servlet-app-1.0.jar
COPY ${JAR_FILE} servlet-app-1.0.jar

# java -jar /usr/local/runme/app.jar
ENTRYPOINT ["java","-jar","servlet-app-1.0.jar"]
