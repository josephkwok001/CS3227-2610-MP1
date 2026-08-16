# Log 08 — JavaFX GUI

**Date:** 16 Aug 2026
**Thread intent:** Finish remaining P0 (JavaFX GUI + fat JAR). No P1 commands.
**Verification:** Agent-drafted. Joseph should run `./gradlew run` and `java -jar build/libs/budgie.jar`.

## Prompts (summary)

1. Finish remaining P0; include a commit-message example, PR description, and reflection draft.

## What was produced

- `Launcher` + `MainApp` + `MainWindow` + `DialogBox` chat UI
- `Budgie.getResponse` shared by CLI and GUI
- `./gradlew run` launches GUI; `./gradlew runCli` keeps the terminal UI
- User Guide v1.0

## Files touched (high level)

- `src/main/java/seedu/budgie/{Budgie,Launcher,MainApp,CommandResult,Messages}.java`
- `src/main/java/seedu/budgie/ui/{MainWindow,DialogBox,Ui}.java`
- `src/main/resources/view/MainWindow.fxml`, `MainWindow.css`
- `build.gradle`, `config/checkstyle/checkstyle.xml`
- docs, README, AGENTS.md

## Checks

- `./gradlew check` — passed (agent)
- GUI window left for Joseph (`./gradlew run`)

## Corrections Joseph should look for

- Increment-01–07 text should be unchanged (agent appended 08–09).
- P1 commands were not added.
