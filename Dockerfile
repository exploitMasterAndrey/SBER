FROM openjdk:20
COPY . .
RUN ./mvnw dependency:go-offline
CMD ["./mvnw", "spring-boot:run"]