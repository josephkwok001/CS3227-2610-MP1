# Log 11 — Find transactions

**Date:** 16 Aug 2026
**Thread intent:** Implement `find KEYWORD` only. No budget, summary, dates, or edit.
**Verification:** Agent `./gradlew check`. Joseph should try `find food` in the GUI.

## Prompts (summary)

1. Implement the first P1 feature, find only.

## What was produced

- `find KEYWORD` matches category or description (case-insensitive substring) or amount (numeric / `$12.50`)
- Result rows keep original `list` numbers for `delete`
- `find` with no keyword → usage; no matches → `No matching transactions found.`
- Help, UG v1.1, DG; unknown-command sample is now `budget 800`
- Tests: Parser find, FindCommand category/description/amount/indexes, help contains find

## Files touched (high level)

- `src/main/java/seedu/budgie/command/FindCommand.java`
- `src/main/java/seedu/budgie/command/HelpCommand.java`
- `src/main/java/seedu/budgie/parser/Parser.java`
- `src/main/java/seedu/budgie/model/{Entry,EntryMatcher,Expense,Income}.java`
- Tests under `src/test/java/seedu/budgie/`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `README.md`, `AGENTS.md`
- `logs/11-find.md`

## Checks

- `./gradlew check` — passed (agent)
- Manual `find food` in the GUI left for Joseph

## Corrections Joseph should look for

- v1.0 GitHub Release JAR does not include `find`; rebuild JAR from this source.
- Increment-01–10 first-person text unchanged (agent appended 11).
- Did not add `budget`, `summary`, dates, or `edit`.
