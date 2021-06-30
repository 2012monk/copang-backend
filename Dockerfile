FROM openjdk:8-jre-alpine

COPY /build/libs/*.jar app.jar

CMD ["java", "-Dspring.profiles.active=prod","-jar", "app.jar"]