# Log 10 — User Guide and Developer Guide rewrite

**Date:** 16 Aug 2026
**Thread intent:** Rewrite UG/DG using the EstateSearch (CS2103 tP) section layout. No new commands. No P1.
**Verification:** Docs only. Joseph should skim the command summary, FAQ, and DG architecture against the running v1.0 JAR.

## Prompts (summary)

1. Update UG and DG using the EstateSearch exemplars: command summary table, format notes, FAQ, glossary; DG architecture (UI/Logic/Model/Storage / MVC), requirements appendix, NFRs.

## What was produced

- UG: getting started via v1.0 Release, command summary table, format notes (2 d.p. amounts), features with existing sample I/O, tester path with GUI `bye` closing the window, FAQ, known issues, glossary
- DG: architecture + sequence for `delete 1`, UI/Logic/Model/Storage, requirements (scope, stories, use cases), NFRs, planned enhancements called out as not shipped
- No `Features.md`. Help text in the app unchanged.

## Files touched

- `docs/UserGuide.md`
- `docs/DeveloperGuide.md`
- `docs/Reflections.md`
- `logs/10-ug-dg-docs.md`

## Checks

- Product behaviour unchanged
- Joseph should confirm UG samples still match `help` / add / list / delete

## Corrections Joseph should look for

- Do not invent `find` / `budget` / `edit` in the UG.
- Increment-01–09 first-person text was not rewritten (agent appended 10 only).

## Reflection notes

I asked Cursor to expand the User Guide and Developer Guide using the same section layout as my CS2103 tP (EstateSearch): command summary table, notes on amounts (two decimal places), FAQ, glossary. DG architecture as UI / Logic / Model / Storage, a sequence for delete, then a requirements appendix (scope, user stories, use cases) and NFRs. I did not ask it to add product features.

Assumptions the LLM made: map AB3 style components onto Budgie / ExpenseBook / Storage / MainWindow rather than inventing LogicManager.

How I verified. Docs rewrite only. Skim command summary, FAQ, and DG architecture against the running v1.0 JAR.

Engineering judgement. Kept v1.0 scope. Planned enhancements listed as not shipped.

Next time. Code quality refactor (shared Expense/Income) if there is spare time. Otherwise polish reflections.
