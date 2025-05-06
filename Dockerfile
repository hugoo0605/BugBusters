FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY BugBusters.jar .

EXPOSE 8080

CMD ["java", "-jar", "BugBusters.jar"]
