FROM openjdk:8-jdk
COPY ./target/authorization.jar authorization.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/authorization.jar"]