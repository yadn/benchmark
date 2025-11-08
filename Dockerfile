# build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
# use the Gradle wrapper to build the fat jar
RUN ./gradlew bootJar --no-daemon -x test

# runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# let container runtime give the port via PORT env var
ENV SERVER_PORT=${PORT:-8080}
ENTRYPOINT ["sh","-c","java -Dserver.port=$SERVER_PORT -jar /app.jar"]
