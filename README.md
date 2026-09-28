# Movies Service

A Spring Boot 4 REST API for movies, directors, actors, and genres.

## Requirements

- Java 21
- Maven 3.9+
- Docker and Docker Compose

## Local development

The `dev/` directory contains `compose.yml`, which starts the PostgreSQL
database required for local development. The database is available on
`localhost:5435`.

```bash
docker compose -f dev/compose.yml up -d
mvn spring-boot:run
```

Stop the database with:

```bash
docker compose -f dev/compose.yml down
```

Flyway applies database migrations automatically when the application starts.

## Build and test

```bash
mvn clean package
mvn test
```

## Development conventions

- Use Java 21 language features and keep the code compatible with Java 21.
- Use Lombok for boilerplate such as getters, constructors, and builders.
- Keep business logic in services, persistence code in repositories, and HTTP
  handling in controllers.
- Add database changes as new Flyway migrations; do not edit migrations that
  have already been applied.
- Add or update tests when changing application behavior.

The application runs on port `8088`. Swagger UI is available at
`/swagger-ui.html`.
