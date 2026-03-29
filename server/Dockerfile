FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew
COPY settings.gradle .
COPY build.gradle .
COPY common/build.gradle common/
COPY server/build.gradle server/
RUN ./gradlew :server:dependencies
COPY common/src common/src
COPY server/src server/src
RUN ./gradlew :server:bootJar -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/server/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]