FROM openjdk:latest
COPY build/libs /jar
WORKDIR /jar
RUN mv *.jar auth_server.jar
ENTRYPOINT [ "java" ]
CMD [ "-jar", "/jar/content_server.jar"]
