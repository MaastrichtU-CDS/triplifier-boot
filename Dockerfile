
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY triplifier/javaTool/src /home/triplifier-app/src
COPY triplifier/javaTool/pom.xml /home/triplifier-app/pom.xml
RUN mvn -f /home/triplifier-app/pom.xml install

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/triplifier-boot-0.0.1-SNAPSHOT.jar /usr/local/lib/triplifier-boot.jar
COPY /src/main/resources/triplifier.properties /config/triplifier.properties
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/triplifier-boot.jar"]
