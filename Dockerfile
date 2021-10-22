FROM openjdk:8u201-jdk-alpine3.9
ADD target/hsbc-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
CMD java -jar hsbc-0.0.1-SNAPSHOT.jar