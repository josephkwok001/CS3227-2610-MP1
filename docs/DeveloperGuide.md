# Budgie Developer Guide

This developer guide describes **v0.1** of Budgie, a personal budget tracker chatbot for CS3227 MP1.

## 1. Setting up

1. Clone the repository and install **Java 17**.
2. Run `./gradlew check` to execute unit tests and Checkstyle.
3. Run `./gradlew run` to start the CLI.

IDE: import the Gradle project. Do not commit IDE-specific files.

## 2. Design overview

v0.1 uses a small command loop:

```mermaid
flowchart LR
  user[User] --> ui[Ui]
  ui --> parser[Parser]
  parser --> command[Command]
  command --> ui
```

| Component | Responsibility |
| --- | --- |
| `Budgie` | Application entry point and command loop |
| `Ui` | Read input, print messages |
| `Parser` | Map a line of text to a `Command` |
| `Command` | Execute and report whether to exit |

Command objects currently include `HelpCommand`, `ExitCommand`, and `UnknownCommand`. Later increments will add expense/income commands and `Storage` without changing this loop.

## 3. Current implementation notes

- Commands are case-insensitive.
- Blank lines are skipped in `Budgie.run()`.
- `Parser` uses an assertion that input is non-null (internal assumption). User-facing errors currently go through `UnknownCommand` rather than exceptions.

## 4. Testing

- JUnit 5 tests live under `src/test/java`.
- `ParserTest` covers `help`, `bye` (including case variations), and unknown input.
- `HelpCommandTest` checks the help text stays consistent with the user-facing copy.
- Run the full gate with `./gradlew check`.
- GUI tests are not applicable yet (CLI only).

CI: GitHub Actions (`.github/workflows/gradle.yml`) runs `./gradlew check` on pushes and pull requests to `master`.

## 5. Software engineering process

Work is **increment-based**: one user-visible feature (or one engineering increment such as CI) per change set.

- `AGENTS.md` records the AI-assisted workflow for this repo.
- After each increment, a session summary is added under `logs/` and a stub is appended to `docs/Reflections.md`. Joseph rewrites stubs in first person.
- GitHub Issues and milestones will be used once increments beyond v0.1 start, so project history stays visible.

## 6. Acknowledgements

- CS2103/T [Project Duke trimmed for CS3227](https://nus-cs2103-ay2627-s1.github.io/website/projectDuke/cs3227.html) AI Guidance (increment style, agent files, test-after-change).
- [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html) and Git commit convention.
- Checkstyle rules adapted from [AddressBook Level 3](https://github.com/se-edu/addressbook-level3).
- Gradle / JUnit / Checkstyle / GitHub Actions tutorials at [se-education.org/guides](https://se-education.org/guides/).
