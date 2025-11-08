# build stage: use Java 21 JDK
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
# build the Spring Boot fat jar (skip tests for faster builds)
RUN ./gradlew bootJar --no-daemon -x test

# runtime stage: use Java 21 JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# let platform supply PORT (fallback to 8080)
ENV SERVER_PORT=${PORT:-8080}
ENTRYPOINT ["sh","-c","java -Dserver.port=$SERVER_PORT -jar /app.jar"]
