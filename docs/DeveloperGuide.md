# Budgie Developer Guide

This developer guide describes **v1.0** of Budgie, a personal budget tracker chatbot for CS3227 MP1.

## 1. Setting up

1. Clone the repository and install **Java 17**.
2. Run `./gradlew check` to execute unit tests and Checkstyle.
3. Run `./gradlew run` to start the JavaFX GUI. Use `./gradlew runCli` for the text-only CLI.

IDE: import the Gradle project. Do not commit IDE-specific files. Runtime data under `data/` is gitignored.

## 2. Design overview

v1.0 uses shared command logic behind a CLI and a JavaFX chat window:

```mermaid
flowchart LR
  user[User] --> gui[MainWindow]
  user --> cli[Ui]
  gui --> budgie[Budgie]
  cli --> budgie
  budgie --> parser[Parser]
  parser --> command[Command]
  command --> book[ExpenseBook]
  budgie --> storage[Storage]
  storage --> book
```

- `Launcher` — JAR / `./gradlew run` entry point; launches `MainApp` without extending `Application`
- `MainApp` / `MainWindow` / `DialogBox` — JavaFX chat UI
- `Ui` — text CLI
- `Budgie` — load storage, `getResponse`, optional `run()` CLI loop
- `Parser` — map a line of text to a `Command`; throw `BudgieException` for bad arguments
- `Command` — execute against `ExpenseBook`; `modifiesData()` is true for add and delete
- `Expense` / `Income` implement `Entry`; `ExpenseBook` stores them in insertion order
- `Storage` — read/write `data/budgie.txt`

## 3. Current implementation notes

- The command word is case-insensitive; expense descriptions keep the user's capitalisation.
- Blank input is skipped in both the CLI loop and the GUI send handler.
- `Parser` uses an assertion that input is non-null (internal assumption).
- Invalid `expense` / `income` format, missing amount, negative amount, bad `delete` indexes, and missing delete index are `BudgieException`s shown to the user.
- Missing amount (`expense` with no amount token, or an argument list that starts with `/category`) is a separate message from the general usage string.
- Negative amounts use `Amount cannot be negative.`; zero and extra decimals share the generic amount message.
- Other unknown text still goes through `UnknownCommand`.
- Save format is one line per transaction: `E|12.50|food|lunch` or `I|2500.00|salary|August pay`. The last field is the rest of the line, so descriptions may contain `|`.
- A missing save file starts an empty book. Invalid lines are skipped and counted. Saving after a mutating command includes the empty-book case so deletes are not undone on the next launch.
- If saving fails, the user still sees the command result plus a save-error line.
- `bye` in the GUI shows the goodbye text, then closes the window after a short delay.
- `./gradlew shadowJar` writes `build/libs/budgie.jar`. Attach that file to a GitHub Release for testers who do not build from source. The JAR bundles JavaFX natives for Windows, Linux, and macOS (including Apple Silicon). Testers still need **Java 17**.

## 4. Testing

- JUnit 5 tests live under `src/test/java`.
- `ParserTest` covers `help`, `bye`, `list`, `delete`, unknown input, missing amount, negative amount, and other valid/invalid `expense` and `income` cases.
- `ListCommandTest` checks empty-book output and mixed insertion order.
- `DeleteCommandTest` checks a valid delete, an out-of-range index message, and delete on an empty book.
- `AddExpenseCommandTest` and `AddIncomeCommandTest` check that execute adds to `ExpenseBook`.
- `HelpCommandTest` checks the help text stays consistent with the User Guide.
- `StorageTest` covers missing file, round-trip (including `|` in a description), save-after-delete, empty file after deleting the last row, and skipped corrupt lines.
- Run the full gate with `./gradlew check`.
- There are no automated GUI tests. Check the window by running `./gradlew run` or `java -jar build/libs/budgie.jar`.

CI: GitHub Actions (`.github/workflows/gradle.yml`) runs `./gradlew check` on pushes and pull requests to `master`.

## 5. Software engineering process

Work is **increment-based**: one user-visible feature (or one engineering increment such as CI) per change set.

- `AGENTS.md` records the AI-assisted workflow for this repo.
- After each increment, a session summary is added under `logs/` and a stub is appended to `docs/Reflections.md`. Joseph rewrites stubs in first person.

## 6. Acknowledgements

- CS2103/T [Project Duke trimmed for CS3227](https://nus-cs2103-ay2627-s1.github.io/website/projectDuke/cs3227.html) AI Guidance (increment style, agent files, test-after-change).
- [SE-EDU Java coding standard](https://se-education.org/guides/conventions/java/intermediate.html) and Git commit convention.
- [SE-EDU JavaFX tutorial](https://se-education.org/guides/tutorials/javaFx.html) (`Launcher` plus fat-JAR classifiers, same idea as AddressBook Level 3).
- Checkstyle rules adapted from [AddressBook Level 3](https://github.com/se-edu/addressbook-level3).
- Gradle / JUnit / Checkstyle / GitHub Actions tutorials at [se-education.org/guides](https://se-education.org/guides/).
