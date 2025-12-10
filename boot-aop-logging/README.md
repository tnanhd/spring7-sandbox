# boot-aop-logging

A tiny Spring Boot example that demonstrates an AOP logging aspect which logs method entry/exit (at DEBUG) for controllers, services, and repositories.

Quick start
-----------

Prerequisites
- Java 17

Run the application (recommended during development)

```bash
# start with the Spring 'dev' profile and enable DEBUG logs for the app package
./gradlew bootRun --args='--spring.profiles.active=dev --logging.level.com.example.aoplogging=DEBUG'
```

Or build and run the jar

```bash
./gradlew bootJar
java -jar build/libs/boot-aop-logging-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --logging.level.com.example.aoplogging=DEBUG
```

What the AOP feature does
-------------------------

- The `LoggingAspect` (in `com.example.aoplogging.aop`) intercepts methods in:
  - controllers (`@RestController`),
  - services (`@Service`), and
  - repositories (`@Repository`).
- On method entry/exit it logs debug messages like "Started <method>() with argument[s] = [...]" and "Finished <method>() with result = ...".

Testing the greeting API
------------------------

This app exposes a simple greeting endpoint handled by `DemoController`.

- Endpoint: GET /api/v1/greeting
- Query parameters are bound to the `DemoDto` record: `id` (Long) and `message` (String).

Examples

```bash
# default greeting (id=1)
curl -s "http://localhost:8080/api/v1/greeting?id=1"
# -> Hello World

# another locale
curl -s "http://localhost:8080/api/v1/greeting?id=2"
# -> Hola Mundo
```

Expected logs (when DEBUG enabled)
----------------------------------

When you call the API with DEBUG logging enabled you'll see entries similar to:

```
DEBUG ... Started greeting() with argument[s] = [DemoDto[id=1, message=null]]
DEBUG ... Finished greeting() with result = Hello World
DEBUG ... Started greetTheWorld() with argument[s] = [1]
DEBUG ... Finished greetTheWorld() with result = Hello World
DEBUG ... Started getGreeting() with argument[s] = [1]
DEBUG ... Finished getGreeting() with result = Hello World
```