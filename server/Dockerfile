FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -q -f pom.xml -DskipTests package
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/target/tinder-mock-server-1.0.0.jar"]
