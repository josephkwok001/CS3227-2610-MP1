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

## Reflection notes

For this increment I asked Cursor to add only expense AMOUNT /CATEGORY DESCRIPTION in memory, and to update the User Guide, help text, tests, a log, and a reflection stub. I told it not to add income, list, delete, persistence, or GUI, and not to commit. The prompt was scoped that tightly so I would have a clear "what did the model assume?" story, and so the User Guide would not describe list before it exists.

Assumptions the model made. I gave the example expense 12.50 /food lunch but not the money type or error policy. It chose BigDecimal with at most two decimal places and a strictly positive amount. It also chose a one-word category after /, and BudgieException for a bad expense instead of "I don't understand". It also changed Command.execute to take an ExpenseBook, and it stopped lowercasing the whole line (increment 01 did that) so descriptions keep capitalisation. Those were reasonable, but they were its design, not a spec I wrote.

What it got wrong / what I had to watch. It did not try to implement the list feature, which is what I was worried about. I still had to confirm the User Guide tells testers that expenses are not listed and not saved. When I ran ./gradlew run, Gradle's progress bar still mixed with the chatbot output, so typing was messy. That is the same CLI issue as increment 01, not a new expense bug.

How I verified. The agent ran `./gradlew check` (parser and add expense tests, Checkstyle). I ran the app myself: a bad command (expense without a description) showed the usage message. Expense with /food beef and rice printed Added expense: $6.80 /food beef and rice. Help listed expense. Another add also worked.

Next time. Put one invalid example in the prompt (expense 12.50 /food with no description) so the UG, help, and tests cannot drift. After check is green, immediately try that invalid line myself.
