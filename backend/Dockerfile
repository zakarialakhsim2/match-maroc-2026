FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Use shell form (not exec form) so env vars expand at runtime
ENTRYPOINT sh -c 'exec java \
  -Dspring.datasource.driver-class-name=org.postgresql.Driver \
  -Dspring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration \
  -jar app.jar'
