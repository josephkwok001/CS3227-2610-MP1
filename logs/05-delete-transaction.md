# Log 05 — Delete transaction

**Date:** 16 Aug 2026
**Thread intent:** Implement only `delete INDEX` (issue #4). No persist or GUI.
**Verification:** Agent-drafted. Joseph should run `./gradlew check` and a manual list → delete → list session.

## Prompts (summary)

1. Implement the delete command only; do not implement other commands.

## What was produced

- `ExpenseBook.delete(int)` using 1-based `list` indexes; expense/income counts updated
- `DeleteCommand`; parser `delete INDEX`
- Help, User Guide v0.5, Developer Guide, README
- Tests: valid delete + renumber, out-of-range, missing/zero index
- `Command.execute` now declares `throws BudgieException`

## Files touched (high level)

- `src/main/java/seedu/budgie/model/ExpenseBook.java`
- `src/main/java/seedu/budgie/command/*` (`DeleteCommand` + `throws` on `execute`)
- `src/main/java/seedu/budgie/parser/Parser.java`
- Tests under `src/test/java/seedu/budgie/`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `README.md`, `AGENTS.md`
- `logs/05-delete-transaction.md`

## Checks

- Intended: `./gradlew check`
- Manual run left for Joseph

## Corrections Joseph should look for

- Increment-01–04 first-person text should be unchanged (agent only appended 05).
- After delete, `list` numbers change. Persist is still not implemented.
