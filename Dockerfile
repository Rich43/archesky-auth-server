FROM openjdk:latest
COPY . /app
WORKDIR /app
RUN apk update
RUN apk add gradle
RUN gradle wrapper
RUN ./gradlew clean bootJar
WORKDIR /app/build/libs
RUN mv -v *.jar auth_server.jar
RUN mkdir /jar
RUN cp -v ./auth_server.jar /jar/
WORKDIR /jar
RUN rm -rfv /app
ENTRYPOINT [ "java" ]
CMD [ "-jar", "/jar/auth_server.jar"]
