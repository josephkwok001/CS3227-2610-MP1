# CS3227 MP1 — Budgie

Budgie is a personal budget tracker with a chat-style interface. This is an individual project for NUS CS3227 Agentic Software Engineering.

## Current version

**v0.2** — greet, `help`, `bye`, and in-memory `expense`. Not yet: income, list, delete, or saving to disk.

## Quick start

Requirements: **Java 17**.

```bash
./gradlew run
```

Run tests and Checkstyle:

```bash
./gradlew check
```

Build a fat JAR (`build/libs/budgie-0.1.jar`):

```bash
./gradlew shadowJar
java -jar build/libs/budgie-0.1.jar
```

## Documentation

- [User Guide](docs/UserGuide.md)
- [Developer Guide](docs/DeveloperGuide.md)
- [Reflections](docs/Reflections.md)
- [Prompt logs](logs/)

## Acknowledgements

Project conventions and tooling follow the CS2103/T iP AI Guidance and [se-edu](https://se-education.org/) tutorials (Gradle, Checkstyle, Java coding standard).
