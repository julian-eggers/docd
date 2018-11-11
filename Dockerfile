FROM openjdk:11-jre-slim
EXPOSE 8080
ADD target/docd.jar docd.jar
ENTRYPOINT ["java", "-jar", "-Dfile.encoding=UTF-8", "-Djava.security.egd=file:/dev/./urandom", "docd.jar"]
