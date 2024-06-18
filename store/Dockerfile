RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
LABEL authors="Cao"

#COPY --from=build /app/target/DrComputer-0.0.1-SNAPSHOT.war drcomputer.war
EXPOSE 8082

ENTRYPOINT ["java","-jar","drcomputer.war"]


