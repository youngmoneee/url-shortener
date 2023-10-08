FROM openjdk:17-jdk

COPY ./build/libs/surl-0.0.1.jar ./surl.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./surl.jar"]
