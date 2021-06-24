FROM openjdk:8-jre-alpine

COPY /build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]