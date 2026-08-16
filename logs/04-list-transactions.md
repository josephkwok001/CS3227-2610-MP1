# Log 04 — List transactions

**Date:** 16 Aug 2026
**Thread intent:** Implement only GitHub issue #3: `list`. No delete, persistence, or GUI.
**Verification:** Agent-drafted. Joseph should run `./gradlew check` and a manual add-then-`list` session.

## Prompts (summary)

1. Implement the third feature #3 list only; not other features.

## What was produced

- `Entry` interface; `Expense` / `Income` implement `toListLine()`
- `ExpenseBook` stores a single insertion-order list
- `ListCommand`; parser `list` (case-insensitive)
- Help, User Guide v0.4, Developer Guide, README
- Tests: empty list, mixed order, parse `list`

## Files touched (high level)

- `src/main/java/seedu/budgie/model/Entry.java`, `Expense.java`, `Income.java`, `ExpenseBook.java`
- `src/main/java/seedu/budgie/command/ListCommand.java`, `HelpCommand.java`
- `src/main/java/seedu/budgie/parser/Parser.java`
- `src/test/java/seedu/budgie/command/ListCommandTest.java`, `HelpCommandTest.java`, `parser/ParserTest.java`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `README.md`, `AGENTS.md`
- `logs/04-list-transactions.md`

## Checks

- Intended: `./gradlew check`
- Manual run left for Joseph

## Corrections Joseph should look for

- Increment-01–03 first-person text should be unchanged (agent only appended 04).
- `delete` is still not a command; list numbers are display-only.
- Empty `list` message must match the UG.
