FROM openjdk:17-jdk

ARG JAR=./build/libs/surl-0.0.1.jar
COPY ${JAR} ./surl.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./surl.jar"]
