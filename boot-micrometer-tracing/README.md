# boot-micrometer-tracing

This is a minimal Spring Boot example demonstrating Micrometer Tracing integration.
It exposes a single endpoint `/api/v1/ping` that logs a message when invoked.

Prerequisites
- JDK 17+ (or the version configured in the project)
- Gradle (optional) — the project includes the Gradle wrapper so you can use `./gradlew`.

Quick start

Build and run with the Gradle wrapper:

```bash
./gradlew bootRun
```

Or build a jar and run it:

```bash
./gradlew clean build
java -jar build/libs/*-SNAPSHOT.jar
```

Trigger the endpoint from another terminal:

```bash
curl -v http://localhost:8080/api/v1/ping
```

Observe the application console logs. You should see a log line similar to:

```
2025-12-10T21:47:46.437+07:00  INFO 40069 --- [boot-micrometer-tracing] [nio-8080-exec-1] [ddffc673c78dde346abb78605586cac1-9fafc74961f61ba1] c.e.b.PingController                     : Received ping request
```
