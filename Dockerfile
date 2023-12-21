FROM openjdk:17
EXPOSE 8080
MAINTAINER gcp.poc
COPY build/libs/weather-data-0.0.1-SNAPSHOT.jar weather-data-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/weather-data-0.0.1-SNAPSHOT.jar"]