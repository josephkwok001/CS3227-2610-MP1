# Log 15 — Budgie integration tests (Tree of Thoughts)

**Date:** 28 Aug 2026  
**Thread intent:** Integration tests for `Budgie.getResponse(String)` — Parser + Command + ExpenseBook + `Storage.save`. Used **Tree of Thoughts (ToT)** to choose test setup before writing code.  
**Verification:** `./gradlew check` passed (79 tests). Joseph should verify log and skim tests.

---

## Why Tree of Thoughts here

`Budgie()` hard-codes `new Storage()` → `data/budgie.txt` relative to the working directory. Integration tests must hit that path without mocking, but also must not read Joseph’s real save file. ToT fits because there are two viable setups with different trade-offs; comparing them **before** implementation avoids a production change Joseph did not approve.

---

## Prompt 1 — Plan only (ToT: approach A vs B)

### Prompt (exact)

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

Design integration tests for Budgie.getResponse(String) covering Parser + Command + ExpenseBook + Storage.save.

Constraint: Budgie() uses Storage() default path data/budgie.txt relative to working directory.

Propose TWO approaches in a short comparison table (pros/cons):
A) Tests that chdir into @TempDir and use default Storage path
B) Small test-only refactor (e.g. package-private constructor Budgie(Storage)) — only if I approve

If I approve approach A only: write BudgieTest with @TempDir, run getResponse for:
- expense 12.50 /food lunch → then list → exact message
- delete 1 after add → file persists (new Budgie() instance → list shows remainder)
- summary after adds → net line present
- budget 800 → unknown command message, book unchanged

Verify modifiesData: find/summary/help do NOT write file; expense does.

Explain integration vs unit boundaries.
PLAN ONLY first — wait for my approval before implementing approach A.
```

### LLM response (ToT comparison table)

| | **A — `@TempDir` + change working directory** | **B — inject `Storage` (test constructor)** |
|---|---|---|
| **How** | Save/restore `user.dir` to `@TempDir`; `new Budgie()` uses `data/budgie.txt` under temp | Add `Budgie(Storage)`; tests pass `new Storage(tempDir.resolve("data/budgie.txt"))` |
| **Pros** | No production changes; exercises real default path | Parallel-safe; explicit path; matches `StorageTest` pattern |
| **Cons** | `user.dir` is JVM-global; flaky under parallel tests; hacky | Needs Joseph’s approval + one prod edit |
| **Recommendation** | Good for tests-only, sequential runs | Better long-term if parallelism matters |

Joseph replied: **“Yes u can implement approach A”** (then clarified logs, not Reflections, should capture ToT).

---

## Prompt 2 — Implement approach A

### Prompt (exact)

Same shared rules block + approval to implement approach A with the scenarios listed above.

### LLM response (summary)

- Created `BudgieTest.java` with six `getResponse_...` methods.
- First attempt: `@TempDir` + `System.setProperty("user.dir", tempDir)`.
- **`./gradlew check` failed** — tests loaded Joseph’s real `data/budgie.txt` (7+ stale entries). Root cause: on Java 17, `Path.of("data/budgie.txt")` does **not** follow runtime `user.dir` changes (default directory cached at JVM start).
- **Fix (still approach A, no prod code):** Gradle `test.workingDir = build/test-run` in `build.gradle`; `@BeforeEach` deletes `data/budgie.txt` for isolation.
- `./gradlew check` green after fix.

---

## Files touched

- `src/test/java/seedu/budgie/BudgieTest.java` (new)
- `build.gradle` (test `workingDir` + mkdir `data/`)
- `logs/15-budgie-integration-tests.md` (this file)

No production code changes. No commit unless Joseph asks.

---

## Test design

| Scenario | Method | Layers crossed | Why it matters |
|---|---|---|---|
| Add expense → list | `getResponse_addExpenseThenList_showsExactMessages` | Parser → Command → Model → Storage | Only test that wires raw strings through to list message **and** file line |
| Delete persists | `getResponse_deleteAfterAdd_persistsForNewBudgieInstance` | Same + `new Budgie()` reload | Proves `modifiesData` + auto-save in `Budgie.getResponse` |
| Summary after adds | `getResponse_summaryAfterAdds_includesNetLine` | Parser → Command (read-only) → Model | Net line via public API |
| Unknown `budget 800` | `getResponse_budgetCommand_leavesBookAndFileUnchanged` | Parser → UnknownCommand | UG example stays unknown; no save file |
| Expense writes disk | `getResponse_addExpense_writesSaveFile` | Budgie save gate | Confirms mutating commands persist |
| Read-only commands | `getResponse_readOnlyCommands_doNotChangeSaveFile` | help / find / summary | File unchanged after seed data |

---

## Integration vs unit boundaries

| Layer | Unit tests already cover | `BudgieTest` adds |
|---|---|---|
| Parser | `ParserTest` | Invoked only via `getResponse("…")` strings |
| Command | `*CommandTest` | Execute path through parse, not direct `new XxxCommand()` |
| Model | `ExpenseBookTest`, etc. | Book changed only via commands |
| Storage | `StorageTest`, `ExpenseBookStorageIntegrationTest` | Save triggered by **`Budgie`**, not direct `storage.save()` |
| Façade | *(none)* | `modifiesData` gate, load-on-construct, `CommandResult` |

---

## What the model got right / wrong

- **Right:** ToT plan-first avoided unapproved `Budgie(Storage)`; message strings match existing unit tests; six scenarios map cleanly to UG behaviour.
- **Wrong:** Assumed `user.dir` repoints relative paths on Java 17 — it does not; first test run failed until Gradle `workingDir` fix.
- **Overlap:** Persistence also covered in `ExpenseBookStorageIntegrationTest`; keep `BudgieTest` for façade + parser + save gate only.
- **Joseph should verify:** `./gradlew check`; skim whether `build.gradle` global `workingDir` is acceptable for the whole suite.

---

## ToT takeaway (for reflections later)

Comparing A vs B **before** coding was useful: when A's `user.dir` hack failed, the ToT table already documented B as the fallback — Joseph could switch without re-deriving the trade-off. Next time, include "Java 17 caches default directory" as a con on approach A in the initial ToT table.

## Reflection notes

After unit tests (increment 14), I wanted integration tests for Budgie.getResponse(String) that cross Parser, Command, ExpenseBook, and Storage.save without mocking the façade. I used a Tree of Thought prompt first: approach A (@TempDir + default data/budgie.txt path, no production changes) vs approach B (inject Storage via a test constructor). I approved A only. The first implementation failed because System.setProperty("user.dir") does not repoint Path.of("data/budgie.txt") on Java 17. The fix stayed within approach A: Gradle test.workingDir = build/test-run plus @BeforeEach cleanup of data/budgie.txt. `./gradlew check` went green with 79 tests.

Assumptions the LLM made: default Storage path; six scenarios covering add/list, delete + reload, summary net line, unknown budget 800, expense writes file, read-only commands skip save.

How I verified. `./gradlew check` (79 tests). BudgieTest does not read Joseph's real save file.

Engineering judgement. Kept approach A after user.dir failure. Did not add Budgie(Storage) without approval. Some overlap with storage integration tests is intentional (façade vs direct storage).

Next time. Put "Java 17 caches default directory" in the ToT table up front. Consider B if parallel test isolation becomes a problem.
