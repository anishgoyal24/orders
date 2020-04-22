FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8081
ARG JAR_FILE=/orders-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} orders-app.jar
ENTRYPOINT ["java","-jar","orders-app.jar"]