# Log 07 — Error messages

**Date:** 16 Aug 2026
**Thread intent:** On the same persist branch, finish the errors bullet only: unknown command, missing amount, negative money, unknown delete index. No GUI.
**Verification:** Agent-drafted. Joseph should type the four cases in `./gradlew run`.

## Prompts (summary)

1. Fix the next backlog bullet (errors) on the same branch.

## What was produced

- Distinct missing-amount messages (`expense` / `income` with no amount token, or args starting with `/category`)
- Distinct `Amount cannot be negative.` (zero and extra decimals still use the generic amount message)
- Unknown-index message extracted to `ExpenseBook.unknownIndexMessage`
- Unknown command message unchanged; tests now assert the exact text
- User Guide v0.7 section 3.8 + peer-test steps 12–16

## Files touched (high level)

- `src/main/java/seedu/budgie/parser/Parser.java`
- `src/main/java/seedu/budgie/model/ExpenseBook.java`
- `src/test/java/seedu/budgie/parser/ParserTest.java`
- `src/test/java/seedu/budgie/command/DeleteCommandTest.java`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `README.md`, `AGENTS.md`
- `logs/07-errors.md`

## Checks

- `./gradlew check` — passed (agent)
- Manual four-case run left for Joseph

## Corrections Joseph should look for

- Increment-01–06 first-person / draft text should be unchanged (agent only appended 07).
- `delete 0` still shows usage, not the unknown-index message.
- GUI was not added.
