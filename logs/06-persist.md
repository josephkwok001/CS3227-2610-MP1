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
