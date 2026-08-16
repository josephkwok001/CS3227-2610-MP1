# Log 12 — Summary command

**Date:** 16 Aug 2026
**Thread intent:** Implement `summary` only. No budget, dates, or edit.
**Verification:** Agent `./gradlew check`. Joseph should try `summary` in the GUI after two sample adds.

## Prompts (summary)

1. Implement the summary command now.

## What was produced

- `summary` shows total income, total expenses, net (income minus expenses), and totals by category
- Empty book uses the same message as `list`
- Same-category amounts are merged; category sections are omitted when that side is empty
- Negative net is shown as `-$x.xx`
- `modifiesData()` is false (no save)
- No remaining-budget line (there is no `budget` command)
- Help, UG/DG/README **v1.2**; unknown-command sample stays `budget 800`
- Tests: Parser summary, SummaryCommand empty/mixed/merge/income-only, help contains summary

## Files touched (high level)

- `src/main/java/seedu/budgie/command/SummaryCommand.java`
- `src/main/java/seedu/budgie/command/HelpCommand.java`
- `src/main/java/seedu/budgie/parser/Parser.java`
- Tests under `src/test/java/seedu/budgie/`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `README.md`, `AGENTS.md`
- `logs/12-summary.md`

## Checks

- `./gradlew check` — passed (agent)
- Manual `summary` in the GUI left for Joseph

## Corrections Joseph should look for

- v1.0 GitHub Release JAR does not include `find` or `summary`; rebuild JAR from this source.
- Increment-01–11 first-person text unchanged (agent appended 12).
- Did not add `budget`, dates, or `edit`.
