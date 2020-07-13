FROM openjdk:latest
COPY . /app
WORKDIR /app
RUN ./build.sh
RUN ./gradlew clean bootJar
WORKDIR /app/build/libs
RUN mv -v *.jar auth_server.jar
RUN mkdir /jar
RUN cp -v ./auth_server.jar /jar/
WORKDIR /jar
RUN rm -rfv /app
RUN rm -rfv /root/.sdkman
RUN yum remove -y which unzip zip
ENTRYPOINT [ "java" ]
CMD [ "-jar", "/jar/auth_server.jar"]
