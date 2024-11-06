FROM openjdk:21

COPY build/libs/*SNAPSHOT.jar /application/app.jar

WORKDIR /application

ENTRYPOINT ["java","-jar","app.jar"]
