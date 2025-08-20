FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

WORKDIR /app/magnum-fipe-api-2
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/magnum-fipe-api-2/target/*-runner.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]