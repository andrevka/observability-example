FROM maven:3.8.3-openjdk-17 as builder

COPY ./pom.xml /application/pom.xml

WORKDIR application

RUN mvn compile

COPY ./ /application/

RUN mvn package -DskipTests

FROM eclipse-temurin:17.0.12_7-jre

WORKDIR application

COPY --from=builder /application/target/*.jar /application/app2.jar

ENTRYPOINT ["java", "-jar", "app2.jar"]