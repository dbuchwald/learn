FROM openjdk:17-jdk-alpine
RUN apk --no-cache add curl jq
ARG JAR_FILE
COPY target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]