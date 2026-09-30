FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY . .
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=60.0 -XX:+UseSerialGC"

ENTRYPOINT ["java", "-jar", "app.jar"]