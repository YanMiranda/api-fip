FROM maven:3.9.8-amazoncorretto-17 AS build
WORKDIR /app
COPY . .

WORKDIR /app/magnum-fipe-api-1
RUN mvn package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app

EXPOSE 8081

COPY --from=build /app/magnum-fipe-api-1/target/quarkus-app/ .

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]