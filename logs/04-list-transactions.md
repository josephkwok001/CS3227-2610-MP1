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

## Reflection notes

I asked Cursor to add only list, not delete or save. It added an Entry interface and one insertion-order list, so mixed adds stay in the order I typed them. Lines look like "1. [expense] $12.50 /food lunch". Empty book: No transactions yet. Add an expense or income first.

Assumptions the LLM made: Entry interface plus one ExpenseBook list preserves mixed add order; extra words after list are ignored like help.

How I verified. `./gradlew check`. Add mixed entries then list. list on a fresh run shows the empty message. help includes list.

Prompting vs hand work. Numbered list formatting is easy for the agent. Choosing insertion order vs "all expenses then all incomes" needed a design call (increment 03 already flagged this).

Engineering judgement. Did not implement delete even though numbers look like indexes. Did not replace Expense/Income with one class, only added Entry.

Next time. If delete is next, reuse these 1-based indexes in the UG so testers are not surprised.
