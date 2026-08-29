# Log 17 — GUI polish and help formatting

**Date:** 29 Aug 2026  
**Thread intent:** Improve JavaFX chat UI readability (ToT → Option B → Option C) and reformat `help` output only. No new commands; no Logic/Parser/Model changes except `HelpCommand.MESSAGE` text.  
**Verification:** `./gradlew check` green. Joseph should run `./gradlew run` and `java -jar release/budgie.jar`.

---

## Prompt flow

1. **ToT (plan only)** — three options: A CSS/FXML only, B CSS + wider `DialogBox`, C BorderPane + TextFlow + speaker dedupe. Joseph approved **B**, then asked to **implement C** to compare visuals.
2. **Option B implemented** — wider bubbles, title bar, input styling (`logs` not written separately; superseded by C).
3. **Option C implemented** — `BorderPane` layout, `TextFlow` for multi-line replies, hide repeated speaker labels, bottom input strip.
4. **Help format** — Joseph screenshot: help list hard to scan. Reformatted `HelpCommand.MESSAGE` only (`command — description`, blank line after intro); `DialogBox` styles command word in monospace bold.

---

## Files touched

| File | Change |
|---|---|
| `src/main/resources/view/MainWindow.fxml` | `BorderPane`: title, scroll chat, bottom input `HBox` |
| `src/main/resources/view/MainWindow.css` | Nest theme, bubbles, input bar, command/separator text styles |
| `src/main/java/seedu/budgie/ui/DialogBox.java` | `TextFlow`, `appendLineToFlow`, help line split on ` — ` |
| `src/main/java/seedu/budgie/ui/MainWindow.java` | Extends `BorderPane`; speaker dedupe via `LastSpeaker` |
| `src/main/java/seedu/budgie/MainApp.java` | Load `Parent`; min window 480×540 |
| `src/main/java/seedu/budgie/command/HelpCommand.java` | `MESSAGE` formatting only (`execute` unchanged) |
| `docs/UserGuide.md` | Help sample + GUI note |
| `docs/DeveloperGuide.md` | UI component section updated |
| `docs/Reflections.md` | Increment 17 stub (Joseph rewrites) |

No new tests (GUI still manual). No commit unless Joseph asks.

---

## Design notes

- **ToT value:** Compared A/B/C before coding; Joseph could pick polish level under time pressure.
- **Help vs GUI:** Help *text* lives in `HelpCommand.MESSAGE`; GUI only styles lines containing ` — `. `list` / `summary` still use monospace for numbered and `/category:` lines.
- **UG/DG:** Help sample must match `HelpCommand.MESSAGE` exactly (including em dash `—`).

---

## Joseph should verify

- `./gradlew check`
- `./gradlew run` → `help` (readable command list), `list`, `summary`, `bye`
- `./gradlew release` then `java -jar release/budgie.jar` — same UI
- User Guide help sample matches app output

---

## What the model got wrong / watch

- Option B `user.dir` not used here; C required small `MainApp` change (not pure ui/ view only).
- Rebuild `release/budgie.jar` after GUI changes before submission.
