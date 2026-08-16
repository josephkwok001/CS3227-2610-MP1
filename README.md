# CS3227 MP1 — Budgie

Budgie is a personal budget tracker with a chat-style JavaFX interface. This is an individual project for NUS CS3227 Agentic Software Engineering.

## Current version

**v1.1** — v1.0 plus `find KEYWORD` (category, description, or amount). Not yet: `budget`, `summary`, dates, `edit`.

## Quick start

Requirements: **Java 17**.

```bash
./gradlew run
```

Run tests and Checkstyle:

```bash
./gradlew check
```

Build a fat JAR (`build/libs/budgie.jar`):

```bash
./gradlew shadowJar
java -jar build/libs/budgie.jar
```

Text-only CLI: `./gradlew runCli`.

## Documentation

- [User Guide](docs/UserGuide.md)
- [Developer Guide](docs/DeveloperGuide.md)
- [Reflections](docs/Reflections.md)
- [Prompt logs](logs/)

## Acknowledgements

Project conventions and tooling follow the CS2103/T iP AI Guidance and [se-edu](https://se-education.org/) tutorials (Gradle, Checkstyle, Java coding standard, JavaFX).
