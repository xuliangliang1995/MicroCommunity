FROM java:8

MAINTAINER xuliangliang

ADD target/*.jar bootstrap.jar

EXPOSE 8008

ENTRYPOINT ["java","-jar","bootstrap.jar"]