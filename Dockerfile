ARG API_MODULE

FROM maven:3.9.8-amazoncorretto-17 AS build

WORKDIR /app
COPY pom.xml .
COPY fipe-domain/pom.xml ./fipe-domain/
COPY magnum-fipe-api-1/pom.xml ./magnum-fipe-api-1/
COPY magnum-fipe-api-2/pom.xml ./magnum-fipe-api-2/

RUN mvn dependency:go-offline

COPY . .

RUN mvn install -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=build /app/${API_MODULE}/target/quarkus-app/ .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]