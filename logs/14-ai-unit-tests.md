# Log 14 — AI-assisted unit tests

**Date:** 28 Aug 2026
**Thread intent:** Add model/parser unit tests using lecture-style prompts (explicit scope, scenario tables, review yourself). Tests only — no production changes.
**Verification:** `./gradlew check` passed after all four prompts. Joseph should skim tests and drop any he considers redundant.

---

## Shared block (prepended to every prompt)

```text
Project: Budgie (CS3227 MP1). Java 17, JUnit 5, Checkstyle. Package: seedu.budgie.
Read AGENTS.md before answering.

Rules:
- Tests only in src/test/java. Do not change production code unless a test exposes a real bug (ask me first).
- Follow existing test style: public class XxxTest, @Test method names like execute_... or parse_...
- Use assertEquals / assertThrows from JUnit 5. No GUI tests.
- Run ./gradlew check and fix Checkstyle.
- Do not commit.
- After generating tests, explain your test design in a short markdown table: scenario | method | why it matters.
```

---

## Prompt 1 — EntryMatcher unit tests

### Prompt (exact)

```text
[Paste shared block]

Develop a comprehensive suite of unit tests for EntryMatcher.matches(BigDecimal, String, String, String) in seedu.budgie.model.

Scope: unit tests only — call EntryMatcher directly, no Parser, no ExpenseBook.

Cover explicitly:
- Category match (case-insensitive substring)
- Description match (case-insensitive substring)
- Amount match: plain 12.50, $12.50, numeric 12.5 vs stored 12.50
- Amount non-match: find 1 must NOT match amount 12.50 (no digit-substring match)
- Empty keyword → false
- Blank keyword after trim → false

Use a structured template for each test:
| Scenario | amount | category | description | keyword | expected |

Generate the JUnit class EntryMatcherTest.java only.
Explain your reasoning: why each case protects a User Guide behaviour.
Do not duplicate scenarios already covered in FindCommandTest unless they test EntryMatcher directly.
STOP after one test class + explanation; no other files.
```

### LLM response (summary)

- Created `EntryMatcherTest.java` with 9 methods (`matches_categoryCaseInsensitiveSubstring_returnsTrue`, amount paths, `find 1` false, empty/blank keyword, `rent` false).
- Returned scenario table linking each case to UG `find` behaviour.
- Did not touch production code.

### Joseph / agent notes

- Good: locks UG rule `find 1` ↛ `$12.50` at model layer, not only via `FindCommandTest`.
- Overlap: category/amount cases mirror `FindCommandTest` but direct `EntryMatcher` calls are still useful for refactor safety.

---

## Prompt 2 — Expand MoneyFormatterTest

### Prompt (exact)

```text
[Paste shared block]

Expand MoneyFormatterTest for seedu.budgie.model.MoneyFormatter.

Add test methods for edge cases not yet covered:
- Positive amounts with 1 vs 2 decimal places (12.5 → 12.50)
- formatSignedDisplay: zero, small negative, positive (no minus)
- Consistency: formatDisplay(amount) equals "$" + formatPlain(amount) for sample values

Use structured cases table before writing code.
Explain why HALF_UP and two decimal places matter for Budgie (UG says $2500.00).
Do not change MoneyFormatter.java unless a test fails for a real bug.
```

### LLM response (summary)

- Expanded from 3 to 9 test methods: one vs two decimal inputs, `formatPlain_halfUp_roundsToTwoDecimals` (`2.555` → `2.56`), `formatDisplay_matchesDollarPlusPlain` loop, small negatives (`-$3.00`, `-$0.01`).
- Explained UG two-decimal display and post-refactor centralisation.
- Minor duplication: `12.5` → `12.50` asserted in more than one method.

### Joseph / agent notes

- Good: consistency test encodes refactor contract (`formatDisplay` = `$` + `formatPlain`).
- Watch: HALF_UP test uses scale-3 input; parser rejects that on user input — test documents formatter only.

---

## Prompt 3 — ExpenseBook.delete unit tests

### Prompt (exact)

```text
[Paste shared block]

Write unit tests for ExpenseBook.delete(int) and ExpenseBook.unknownIndexMessage(int).

Scenarios:
- Valid delete on mixed expense/income list (counts update correctly)
- Index 0, negative, or non-integer path is NOT here (Parser handles delete 0)
- Index too large → BudgieException with exact message from unknownIndexMessage
- Delete on empty book

Template: scenario | setup | index | expected exception or removed entry.

Explain reasoning. Class: ExpenseBookTest.java (new).
Do not test Storage or Parser in this class.
```

### LLM response (summary)

- Created `ExpenseBookTest.java` (5 tests): `unknownIndexMessage`, mixed-list delete (middle income), first expense delete, index 99, empty book.
- Used `book.add()` directly, not commands.
- Explicitly skipped `delete 0` per prompt.

### Joseph / agent notes

- Good: tests `incomeCount()` / `size()` separately after deleting income from mixed list.
- Overlap with `DeleteCommandTest` — Joseph may keep both (model vs command) or trim later.

---

## Prompt 4 — Parser amount validation (consolidate)

### Prompt (exact)

```text
[Paste shared block]

Add focused unit tests to ParserTest for amount validation only (expense/income):

- Zero amount → AMOUNT_INVALID
- Too many decimals → AMOUNT_INVALID  
- Negative → AMOUNT_NEGATIVE (distinct message)
- Missing amount: expense /food lunch → EXPENSE_MISSING_AMOUNT

Do not add new commands. Consolidate with existing ParserTest methods — remove duplication if you add overlapping tests.
Provide a before/after list of which scenarios are covered.
Explain why negative vs zero must stay different messages (User Guide Errors section).
```

### LLM response (summary)

- Merged five separate expense/income amount tests into `parse_expense_invalidAmounts` and `parse_income_invalidAmounts`.
- Renamed missing-amount methods for clarity.
- **Added new coverage:** income `12.555` → `AMOUNT_INVALID` (was missing before).
- First `./gradlew check` failed Checkstyle (method name too many segments, `SeparatorWrap`, `LineLength`); fixed with shorter method names and `assertThrows` line breaks + input variables.

### Joseph / agent notes

- Good: consolidation + before/after table; ties tests to UG Errors peer-test steps.
- LLM caught a real gap (income too many decimals untested).
- Judgement still needed: Checkstyle fixes were not automatic on first pass.

---

## Files touched

| File | Action |
|---|---|
| `src/test/java/seedu/budgie/model/EntryMatcherTest.java` | Created |
| `src/test/java/seedu/budgie/model/MoneyFormatterTest.java` | Expanded |
| `src/test/java/seedu/budgie/model/ExpenseBookTest.java` | Created |
| `src/test/java/seedu/budgie/parser/ParserTest.java` | Consolidated amount validation |
| `docs/Reflections.md` | Increment 14 stub appended |
| `logs/14-ai-unit-tests.md` | This file |

## Checks

- `./gradlew check` — passed after Prompt 4 Checkstyle fixes
- No production code changed
- Not committed (unless Joseph commits separately)

## What Joseph should verify

- Skim tests for redundancy with `FindCommandTest` / `DeleteCommandTest`.
- Confirm prompt text matches what he actually pasted in Cursor.

## Reflection notes

After the refactor pipeline (log 13), I tried the lecture's AI assisted unit testing style: a shared rules block, then four focused prompts, EntryMatcher, expanded MoneyFormatterTest, ExpenseBook.delete, and consolidated Parser amount validation. I asked for scenario tables, UG linked cases, and "tests only, no production code." The model produced sensible tests quickly and even found a gap (income 12.555 was not covered before). It also duplicated some scenarios already in FindCommandTest / DeleteCommandTest, and the Parser consolidation failed Checkstyle on the first run until it refactored assertThrows lines.

How I verified. `./gradlew check` green after Checkstyle fixes.

Engineering judgement. I should review and delete weak/duplicate tests myself. The lecture says review and consolidate, not accept every generated @Test.

Next time. One "test design only" prompt before code. Full analysis is in Deep dive 4 in docs/Reflections.md.
