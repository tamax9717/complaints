# complaints

A complaints management app built with Kotlin and Spring Boot. Mostly a project to get more comfortable with the stack and practice clean architecture.

## Tech stack

- Java 21
- Kotlin
- Spring Boot
- PostgreSQL
- Apache Kafka
- Docker
- Testcontainers
- Unit tests
- SOLID principles

## Getting started

### Prerequisites

- Docker and Docker Compose
- JDK 21

### Run the app

```bash
docker-compose up -d
./gradlew bootRun
```

The `docker-compose up` will spin up PostgreSQL and Kafka. Once those are running, start the app with `gradlew bootRun`.

On Windows use `gradlew.bat bootRun`.

## Running tests

```bash
./gradlew test
```

Tests use Testcontainers so Docker needs to be running when you execute them.

## Project structure

```
complaints/
└── src/
    ├── main/
    │   ├── kotlin/com/example/complaint/
    │   │   ├── domain/
    │   │   │   ├── model/
    │   │   │   └── port/
    │   │   │       ├── in/
    │   │   │       └── out/
    │   │   ├── application/
    │   │   ├── adapter/
    │   │   │   ├── in/rest/
    │   │   │   └── out/
    │   │   │       ├── persistence/
    │   │   │       └── messaging/
    │   │   └── config/
    │   └── resources/
    │       └── db/migration/
    └── test/
        ├── kotlin/com/example/complaint/
        └── resources/
```

## TODO
- [ ] Finish implementing hard delete
- [ ] create missing API calls, like get all
- [ ] create more unit tests to go over all usecases
## Notes

This is a learning/portfolio project. The focus was on clean code, SOLID principles, and working with event-driven patterns via Kafka alongside a standard Spring Boot + PostgreSQL setup.
