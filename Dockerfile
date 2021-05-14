FROM adoptopenjdk/openjdk11:jre-11.0.10_9-alpine
COPY ./target/ecosystem-customer-service.jar /
EXPOSE 32020
ENTRYPOINT ["java", "-jar", "./ecosystem-customer-service.jar"]
