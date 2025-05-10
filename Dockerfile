FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY /target/music-service-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]