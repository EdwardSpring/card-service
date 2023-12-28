FROM maven:3.8.5-openjdk-17-slim
RUN mkdir /app
WORKDIR /app
COPY pom.xml /app/pom.xml
RUN mvn dependency:copy-dependencies
COPY src /app/src
RUN mvn clean install -Dmaven.test.skip=true
COPY target /app/target
RUN  cp target/card-service-latest.jar /app/app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]