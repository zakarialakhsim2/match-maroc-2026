FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", \
  "-Dspring.datasource.url=${DB_URL}", \
  "-Dspring.datasource.username=${DB_USER}", \
  "-Dspring.datasource.password=${DB_PASSWORD}", \
  "-Dspring.datasource.driver-class-name=org.postgresql.Driver", \
  "-Dspring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration", \
  "-jar", "app.jar"]
