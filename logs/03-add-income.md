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

## Reflection notes

I asked Cursor to add only income with the same syntax as expense (income 2500 /salary August pay), and not list, delete, or save. I wanted one issue, one PR, and a User Guide that still says list is not a feature.

Assumptions the LLM made: reuse the expense argument parser and the same amount rules; add a separate Income class rather than a shared Transaction type; display 2500 as $2500.00; invalid income uses INCOME_USAGE, not the expense usage string.

How I verified. Unit tests and `./gradlew check`. Manual income 2500 /salary August pay, income 2500 /salary (missing description), help lists income, list is still unknown.

Prompting vs hand work. Copying expense into income is fast for the agent. Choosing not to introduce list still has to be in the prompt.

Engineering judgement. Shared parseEntry / parseAmount instead of duplicating regex. Kept two model classes for a smaller diff.

Next time. Decide up front whether income/expense should already be one Transaction type, so list does not have to merge two lists.
