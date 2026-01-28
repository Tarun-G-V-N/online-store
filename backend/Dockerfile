FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY src ./src
RUN ./mvnw package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/onlineStore-0.0.1-SNAPSHOT.jar"]