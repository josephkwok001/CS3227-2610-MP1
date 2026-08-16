# Log 03 — Add income in memory

**Date:** 16 Aug 2026
**Thread intent:** Implement only GitHub issue #2 / increment 03: `income 2500 /salary August pay`. Same syntax as expense. No list, delete, persistence, or GUI.
**Verification:** Agent-drafted. Joseph should run `./gradlew check` and a manual `income` / invalid `income` / `help` session.

## Prompts (summary)

1. Implement add income only, same syntax as expense (`income 2500 /salary August pay`).
2. Do not implement other features.

## What was produced

- `Income` model; `ExpenseBook.add(Income)` / `incomeCount()`
- `AddIncomeCommand`; parser `income` case sharing `parseEntry` / `parseAmount` with expense
- Help, User Guide (v0.3), Developer Guide, README
- Tests for valid income, case, missing description, zero amount
- Unknown-command test now uses `list` instead of `income 2500`

## Files touched (high level)

- `src/main/java/seedu/budgie/model/Income.java`, `ExpenseBook.java`
- `src/main/java/seedu/budgie/command/AddIncomeCommand.java`, `HelpCommand.java`
- `src/main/java/seedu/budgie/parser/Parser.java`
- `src/test/java/seedu/budgie/parser/ParserTest.java`, `command/AddIncomeCommandTest.java`, `command/HelpCommandTest.java`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `README.md`, `AGENTS.md`
- `logs/03-add-income.md`

## Checks

- Intended: `./gradlew check`
- Manual run left for Joseph

## Corrections Joseph should look for

- Increment-01 and 02 first-person text must be unchanged (agent only appended 03).
- `list` is still not a feature.
- Whole-dollar amounts display with two decimals (`$2500.00`).
