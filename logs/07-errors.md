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

## Reflection notes

For this increment I asked Cursor to finish the error handling bullet on the same persist branch: unknown command, missing amount, negative money, and unknown delete index. Some of those already existed as generic usage strings. What I wanted was four messages a peer tester can check against the User Guide. The model treated missing amount as expense with no amount token or a line that starts with /category, and it split negative amounts away from zero / too many decimals. That split is a design call I should own.

Assumptions the LLM made: missing amount for empty args or first token starting with /; negative uses Amount cannot be negative; unknown index and unknown command messages unchanged from prior increments.

How I verified. `./gradlew check`. Type find food, expense /food lunch, expense -1 /food lunch, delete 99 and match the User Guide.

Engineering judgement. Did not add GUI. Did not turn zero amounts into the negative message. Did not treat delete 0 as unknown index (still usage).

Next time. JavaFX GUI.
