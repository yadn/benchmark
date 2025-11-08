# build stage: use Java 21 JDK
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon -x test

# runtime stage: use Java 21 JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
# copy the produced jar to a known path
COPY --from=build /app/build/libs/*.jar /app/app.jar
ENV SERVER_PORT=${PORT:-8080}
ENTRYPOINT ["sh","-c","java -Dserver.port=$SERVER_PORT -jar /app/app.jar"]
