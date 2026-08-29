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

- Option B user.dir not used here; C required small MainApp change (not pure ui/ view only).
- release/budgie.jar was rebuilt in the same commit as the GUI polish (531ba6d).

## Reflection notes

I used a ToT prompt to compare three GUI polish levels (CSS only vs wider bubbles vs BorderPane + TextFlow). I approved Option B first, then asked to try Option C so I could see the difference. C added a title bar, bottom input strip, TextFlow bubbles, and hiding repeated "Budgie" / "You" labels. The help list was still hard to read, so I reformatted only HelpCommand.MESSAGE to use command then description lines (with an em dash between them in the app) and styled command words in the GUI. I did not change other commands or add buttons.

Assumptions the LLM made: AddressBook style chat kept; no command buttons; help em dash format; small MainApp tweak for BorderPane.

How I verified. `./gradlew check` green. No GUI tests. release/budgie.jar updated in the same PR.

Engineering judgement. Did not add WebView, avatars, or new commands. Help text change required UG sync.

Next time. Pick one polish option before implementing both B and C. Full analysis is in Deep dive 3 in docs/Reflections.md.
