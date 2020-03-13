FROM java:8

MAINTAINER xuliangliang

ADD ../target/*.jar bootstrap.jar

EXPOSE 8443

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=dev", "bootstrap.jar"]