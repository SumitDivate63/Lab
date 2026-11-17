FROM eclipse-temurin:17-jdk AS build

WORKDIR /app
COPY src ./src
RUN mkdir -p build \
    && javac -d build src/main/java/com/example/helloweb/HelloWebApplication.java \
    && jar --create --file app.jar --main-class com.example.helloweb.HelloWebApplication -C build .

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/app.jar ./app.jar
RUN useradd --system --create-home appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
