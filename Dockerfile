FROM openjdk:latest
COPY . /app
WORKDIR /app
RUN yum update -y
RUN yum install -y which unzip zip
RUN curl -s "https://get.sdkman.io" | bash
RUN source "/root/.sdkman/bin/sdkman-init.sh"
RUN sdk install gradle
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
