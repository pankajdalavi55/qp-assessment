FROM openjdk:17-alpine
WORKDIR /app
COPY target/qp-assessment-0.0.1-SNAPSHOT.jar .
ENV PORT 8087
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "qp-assessment-0.0.1-SNAPSHOT.jar"]