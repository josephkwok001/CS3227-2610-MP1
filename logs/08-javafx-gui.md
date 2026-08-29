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

## Reflection notes

For this increment I asked Cursor to finish remaining P0: a JavaFX chat UI with a Launcher class, plus a fat JAR. I kept the same commands. The GUI is another front end on Budgie.getResponse, not a second parser. The model copied the SE-EDU Launcher pattern so java -jar does not die with "JavaFX runtime components are missing." Chat bubbles and CSS are product choices I should own.

Assumptions the LLM made: ./gradlew run opens GUI; ./gradlew runCli keeps the old terminal; bubbles instead of Duke avatar PNGs.

How I verified. `./gradlew check`. ./gradlew run opens a window. Type help, add, list, bye closes the window.

Engineering judgement. Did not add budget/summary/find. Did not write automated GUI tests.

Next time. Upload budgie.jar as a GitHub Release after merge.
