# Log 02 — Add expense in memory

**Date:** 16 Aug 2026
**Thread intent:** Implement only increment 02: `expense 12.50 /food lunch` in memory. No income, list, delete, persistence, or GUI. Do not commit.
**Verification:** Agent-drafted. Joseph should run `./gradlew check` and a manual `expense` / invalid `expense` / `help` / `bye` session.

## Prompts (summary)

1. Implement only increment 02 with the exact command format.
2. Follow AGENTS.md: UG + help, JUnit, `./gradlew check`, `logs/02-add-expense.md`, reflection stub without touching increment-01 text.
3. Do not create a branch or commit.

## What was produced

- `Expense` + in-memory `ExpenseBook`
- `AddExpenseCommand`; `Command.execute(ExpenseBook)`
- Parser: first-word `expense`, arguments `AMOUNT /CATEGORY DESCRIPTION`
- `BudgieException` for invalid format / amount
- Help, User Guide (v0.2), Developer Guide, README updated
- Tests: `ParserTest` expense cases, `AddExpenseCommandTest`
- Reflection stub appended as increment 02

## Files touched (high level)

- `src/main/java/seedu/budgie/Budgie.java`, `parser/Parser.java`, `command/*`
- `src/main/java/seedu/budgie/model/*`, `exception/BudgieException.java`
- `src/test/java/seedu/budgie/parser/ParserTest.java`, `command/AddExpenseCommandTest.java`, `command/HelpCommandTest.java`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `README.md`
- `logs/02-add-expense.md`

## Checks

- Intended: `./gradlew check`
- Manual run left for Joseph (`./gradlew run`)

## Corrections Joseph should look for

- Increment-01 reflection must still be his first-person text (agent only appended 02).
- Product cannot `list` or save expenses yet; UG says so.
- Command word is case-insensitive; description is not.
