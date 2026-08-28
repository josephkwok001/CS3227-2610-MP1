# Log 13 — Refactor money formatting (prompt pipeline experiment)

**Date:** 28 Aug 2026
**Thread intent:** Refactor only — shared `MoneyFormatter`. Three separate prompts: ToT → few-shot CoT plan → zero-shot implement.
**Verification:** `./gradlew check` passed (52 tests, Checkstyle). Joseph should run manual find/summary smoke tests.

---

## Message 1 — Tree of Thoughts (design only, no code)

**Phase label:** ToT / design exploration  
**Outcome:** Four options compared; **Option A (`MoneyFormatter` in `model`)** recommended. Joseph approved Option A for Message 2.

### Prompt (exact)

```text
Project: Budgie (CS3227 MP1). Java 17, Gradle, JUnit, Checkstyle. Package: seedu.budgie.
Read AGENTS.md and docs/Reflections.md before answering.

Hard rules for this task:
- Refactor only: shared money formatting / reduce duplication. No new user-facing features.
- Do NOT add budget, dates, edit, or any new commands.
- Do NOT merge Expense and Income into one class unless the design phase explicitly chooses that and I approve it.
- User-visible strings must stay identical (e.g. $12.50, $2500.00, -$15.50 for negative net in summary).
- find amount matching must still work (12.5 matches $12.50; find 1 does NOT match $12.50).
- ./gradlew check must pass (JUnit + Checkstyle).
- Do not create Features.md. Do not commit.
- Follow SE-EDU intermediate Java standard.

Phase: DESIGN ONLY (Tree of Thoughts). Do not write or suggest code patches yet.

Task: Refactor duplicated money formatting in Budgie.

Current duplication (inspect these files in the repo):
- seedu.budgie.model.Expense — toDisplayString(), toFileString()
- seedu.budgie.model.Income — same pattern
- seedu.budgie.model.EntryMatcher — amount formatting for find
- seedu.budgie.command.SummaryCommand — private formatMoney() including negative net (-$x.xx)

Step 1 — Tree of Thoughts:
Propose at least THREE distinct design options, for example:
A) Small utility class (e.g. MoneyFormatter) with static format methods
B) Shared helper in model package only; commands still call Entry methods
C) Abstract base class for Expense/Income (larger change)
(D) Any other serious option you see)

For EACH option, give:
- Short description
- Files to add/change
- Pros / cons for THIS codebase (not generic)
- Risk to tests, storage format (E|… / I|… lines), find matching, summary negative net
- Fit with existing ui / parser / command / model / storage split

Step 2 — Comparison:
Rank options for a student MP1 where Checkstyle must stay green and diff should stay small.

Step 3 — Checklist:
Produce a Budgie-specific AI code-review checklist for this refactor (8–12 bullets): intent, tests, UG drift, no new deps, AI pitfalls, etc.

Step 4 — Recommendation:
State which option you recommend and why.

STOP. Do not implement. Do not output Java code blocks longer than a one-line signature example.
```

### Agent response (summary)

- **Options:** A `MoneyFormatter` utility; B extend `EntryMatcher` for formatting (rejected — mixed concerns); C abstract `Expense`/`Income` base (too large); D `Money` value object (too large); E Parser/Storage `parseAmount` (deferred — different increment).
- **Rank:** A best for small diff and low regression risk.
- **Checklist:** 12 Budgie-specific review bullets (intent, tests, UG, find `1` vs `$12.50`, negative net, no new deps, etc.).
- **Recommendation:** Option A with `formatPlain`, `formatDisplay`, `formatSignedDisplay`.

---

## Message 2 — Few-shot chain-of-thought (plan only, no code)

**Phase label:** Few-shot CoT / implementation planning  
**Input from Message 1:** Joseph chose Option A (see choice block below).  
**Outcome:** Numbered implementation plan (steps 1–11) approved for Message 3.

### Prompt (exact)

```text
Project: Budgie (CS3227 MP1). Java 17, Gradle, JUnit, Checkstyle. Package: seedu.budgie.
Read AGENTS.md and docs/Reflections.md before answering.

Phase: PLAN ONLY (few-shot chain-of-thought). Do not write implementation code yet.

Hard rules for this task:
- Refactor only: shared money formatting / reduce duplication. No new user-facing features.
- Do NOT add budget, dates, edit, or any new commands.
- Do NOT merge Expense and Income into one class unless the design phase explicitly chooses that and I approve it.
- User-visible strings must stay identical (e.g. $12.50, $2500.00, -$15.50 for negative net in summary).
- find amount matching must still work (12.5 matches $12.50; find 1 does NOT match $12.50).
- ./gradlew check must pass (JUnit + Checkstyle).
- Do not create Features.md. Do not commit.
- Follow SE-EDU intermediate Java standard.

Chosen design from ToT (I will paste my choice below):
---
I choose Option A (MoneyFormatter in seedu.budgie.model).

Reason: smallest diff, keeps Expense and Income as separate classes (honours increment 03), fixes all four duplication sites, and does not touch Parser/Storage or user-visible behaviour. Parser/Storage parseAmount sharing is deferred to a later increment.
---

Few-shot examples of how I scope increments in this project:

Example 1 — scoped feature prompt (from logs/11-find.md):
"Thread intent: Implement find KEYWORD only. No budget, summary, dates, or edit."
Outcome: FindCommand + Parser case + EntryMatcher; tests in FindCommandTest and ParserTest; help/UG updated; modifiesData() false.

Example 2 — scoped increment prompt (from logs/02-add-expense.md):
"Implement only increment 02: expense in memory. No income, list, delete, persistence, GUI. Follow AGENTS.md: UG, JUnit, ./gradlew check, log, reflection stub."

Example 3 — good command pattern in this repo:
- Parser adds a case word → returns a Command
- Command.execute(ExpenseBook) returns user string
- ParserTest asserts command type + edge cases
- No GUI tests

Now reason step by step (show your reasoning explicitly):

1. Restate the refactor goal and non-goals.
2. List every file to create, modify, or delete (exact paths).
3. Order of edits (what to change first so the project still compiles).
4. How to centralize formatting without changing:
   - list / add / delete messages
   - save file lines (E|12.50|food|lunch)
   - find keyword matching on amounts
   - summary negative net format (-$15.50)
5. Tests: which existing tests prove no behaviour change; which new test(s) if any (e.g. MoneyFormatterTest).
6. What NOT to touch (Parser, Storage, GUI, User Guide — unless you prove output changed).
7. Verification steps: ./gradlew check, and 3–5 manual commands to type.
8. Risks and how to detect regression.

Output a numbered implementation plan I can approve. STOP — no full class implementations.
```

### Agent response (summary)

- **Goal:** Centralise HALF_UP 2 d.p. formatting; keep `Expense`/`Income` separate.
- **Files:** Create `MoneyFormatter.java`, `MoneyFormatterTest.java`; edit `Expense`, `Income`, `EntryMatcher`, `SummaryCommand`; log + reflection stub.
- **Edit order:** Formatter → formatter tests → Expense/Income → EntryMatcher → SummaryCommand → `./gradlew check`.
- **Tests:** Existing command/storage tests must pass unchanged; new `MoneyFormatterTest` for plain/display/signed/negative/zero.
- **Do not touch:** Parser, Storage, UI, UG, help.
- **Plan:** 11 numbered steps ending with log `13` and reflection increment 13; no commit.

---

## Message 3 — Zero-shot implement (plan-conditioned; no new design)

**Phase label:** Zero-shot relative to few-shot examples — only approved plan + hard rules  
**Input from Message 2:** Full numbered plan (Option A, steps 1–11).  
**Outcome:** Code implemented; `./gradlew check` green; no commit.

### Prompt (exact)

```text
Project: Budgie (CS3227 MP1). Java 17, Gradle, JUnit, Checkstyle. Package: seedu.budgie.
Read AGENTS.md and docs/Reflections.md before answering.

Hard rules for this task:
- Refactor only: shared money formatting / reduce duplication. No new user-facing features.
- Do NOT add budget, dates, edit, or any new commands.
- Do NOT merge Expense and Income into one class unless the design phase explicitly chooses that and I approve it.
- User-visible strings must stay identical (e.g. $12.50, $2500.00, -$15.50 for negative net in summary).
- find amount matching must still work (12.5 matches $12.50; find 1 does NOT match $12.50).
- ./gradlew check must pass (JUnit + Checkstyle).
- Do not create Features.md. Do not commit.
- Follow SE-EDU intermediate Java standard.

Phase: IMPLEMENT ONLY (zero-shot relative to examples — use only the approved plan below, not new design ideas).

Approved implementation plan: Option A MoneyFormatter — follow the numbered plan from Chat 2 (steps 1–11). Implement exactly; run ./gradlew check; no commit.

Instructions:
- Implement exactly this plan.
- Read AGENTS.md, docs/Reflections.md, and the files listed in the plan before editing.
- Run ./gradlew check and fix all failures.
- Do NOT add budget, dates, edit, or new commands.
- Do NOT change docs/UserGuide.md unless you can prove user-visible output changed (it should not).
- Append a factual stub to docs/Reflections.md for increment 13 (do not overwrite Joseph's first-person text in earlier increments).
- Write logs/13-refactor-money-formatting.md summarizing prompts, files touched, tests run, what went wrong.
- Do not git commit.

When done, report: files changed, test count, check result, and any manual checks I should run.
```

### Agent response (summary)

- Implemented `MoneyFormatter` with `formatPlain`, `formatDisplay`, `formatSignedDisplay`.
- Updated `Expense`, `Income`, `EntryMatcher`, `SummaryCommand` per plan.
- Added `MoneyFormatterTest` (3 tests).
- **No** UG/help changes.
- `./gradlew check`: **52 tests passed**, Checkstyle green.
- Nothing went wrong in automation; manual GUI/JAR smoke left for Joseph.

---

## Pipeline handoff (what was pasted between messages)

| From | To | Content |
|---|---|---|
| Message 1 → 2 | ToT choice | “I choose Option A (MoneyFormatter…)…” block |
| Message 2 → 3 | Approved plan | “Option A MoneyFormatter — steps 1–11” (full plan in agent reply, not re-pasted in Message 3 prompt) |
| Message 3 | — | Agent read repo + plan from conversation context |

**Note for reflections:** Message 3 is **plan-conditioned implement**, not literal zero-shot with zero context. Honest label: zero-shot at implement = no new few-shot examples, only hard rules + approved plan.

---

## What was produced (code)

- `MoneyFormatter`: `formatPlain`, `formatDisplay`, `formatSignedDisplay` (HALF_UP, 2 d.p.)
- `Expense` / `Income` use formatter for display and file strings
- `EntryMatcher` uses formatter for plain/`$` compare; `BigDecimal.compareTo` path kept for `12.5` vs `12.50`
- `SummaryCommand` uses `formatSignedDisplay`; removed private `formatMoney`
- `MoneyFormatterTest` (3 tests)
- Parser / Storage untouched

## Files touched (high level)

- `src/main/java/seedu/budgie/model/MoneyFormatter.java` (new)
- `src/main/java/seedu/budgie/model/{Expense,Income,EntryMatcher}.java`
- `src/main/java/seedu/budgie/command/SummaryCommand.java`
- `src/test/java/seedu/budgie/model/MoneyFormatterTest.java` (new)
- `docs/Reflections.md` (increment 13 stub appended)
- `logs/13-refactor-money-formatting.md`

## Checks

- `./gradlew check` — passed (52 tests, Checkstyle green)
- Manual `find 1` / `find 12.5` / negative `summary` net left for Joseph

## Corrections Joseph should verify

- Prompt text above matches what you actually sent in Cursor (edit if you trimmed anything).
- Rebuild JAR (`./gradlew shadowJar`) before `java -jar` demo if needed.
- Parser/Storage `parseAmount` duplication still exists — deferred.
- Did not add budget, dates, or edit.
