# Log 06 — Persist to file

**Date:** 16 Aug 2026
**Thread intent:** Implement only persistence (issue #5). No GUI and no extra commands.
**Verification:** Agent-drafted. Joseph should run `./gradlew check` and a restart `list` check.

## Prompts (summary)

1. Implement the next feature only (persist), and include a small reflection draft.

## What was produced

- `Storage` reads/writes `data/budgie.txt` (`E|amount|category|description` / `I|...`)
- Load on start; save after `expense`, `income`, and `delete` (including empty book)
- Missing file → empty list; invalid lines skipped with a warning
- User Guide v0.6 documents auto-save and a peer-test restart step
- Tests: missing file, round-trip, save-after-delete, empty file, corrupt lines

## Files touched (high level)

- `src/main/java/seedu/budgie/storage/Storage.java`
- `src/main/java/seedu/budgie/Budgie.java`
- `src/main/java/seedu/budgie/command/Command.java` (`modifiesData`)
- `src/main/java/seedu/budgie/model/{Entry,Expense,Income,ExpenseBook}.java`
- `src/test/java/seedu/budgie/storage/StorageTest.java`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `README.md`, `AGENTS.md`
- `logs/06-persist.md`

## Checks

- `./gradlew check` — passed (agent)
- Manual restart left for Joseph (`list` after `bye` should keep remaining rows)

## Corrections Joseph should look for

- Increment-01–05 first-person text should be unchanged (agent only appended 06).
- Persistence saves the live book after deletes, not a second snapshot.
- GUI / budget / summary / find were not added.

## Reflection notes

For this increment I asked Cursor to add file persistence only, no GUI and no new commands. I wanted it to save the live list after each add or delete, including after a delete, so a restart would not bring deleted rows back. The model chose data/budgie.txt and a Duke style line format (E|amount|category|description). That is a product choice I should own, not something the assignment stated.

Assumptions the LLM made: path data/budgie.txt relative to working directory; line format E|12.50|food|lunch / I|2500.00|salary|August pay; save immediately after mutating commands including empty file after deleting everything; no save command; help text unchanged.

How I verified. Unit tests and `./gradlew check`. Manual restart path: add, delete, bye, run again, list shows remaining rows only.

Engineering judgement. Did not add GUI. Did not snapshot independently of deletes. Skipped corrupt lines instead of refusing to start.

Next time. Remaining increment is extra error handling, then JavaFX.
