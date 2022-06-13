FROM openjdk:8-jdk
COPY ./target/tweet-service.jar tweet-service.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/tweet-service.jar"]