FROM  eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY build.gradle gradlew ./
COPY gradle ./gradle
COPY ./src ./src

RUN chmod +x gradlew
RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=builder ./app/build/libs/*T.jar  ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]