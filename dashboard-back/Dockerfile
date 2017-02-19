FROM maven:3.3.9-jdk-8

EXPOSE 8080

COPY . /data

WORKDIR /data

RUN mvn -DskipTests package

CMD ["java", "-jar", "target/dashboard-back-1.0.jar"]