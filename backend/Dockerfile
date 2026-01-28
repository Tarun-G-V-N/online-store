# ---------- build stage ----------
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /build
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw
COPY src src
RUN ./mvnw clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /build/target/onlineStore-0.0.1-SNAPSHOT.jar app.jar

# DO NOT hardcode port
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
