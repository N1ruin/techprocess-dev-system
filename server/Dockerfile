FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew

COPY build.gradle .

RUN echo "rootProject.name = 'techprocess-dev-system'" > settings.gradle && \
    echo "include 'server'" >> settings.gradle && \
    echo "include 'common'" >> settings.gradle

COPY common common

COPY server/build.gradle server/
COPY server/src server/src

RUN ./gradlew :server:bootJar -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/server/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]