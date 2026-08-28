# Log 15 — Budgie integration tests (approach A)

**Date:** 28 Aug 2026  
**Thread intent:** Integration tests for `Budgie.getResponse(String)` using real default save path; plan-first with Tree of Thoughts (ToT) for approach selection.  
**Verification:** `./gradlew check` (Joseph should confirm).

---

## Prompt flow

1. **Plan-only prompt** — asked for integration tests covering Parser + Command + ExpenseBook + `Storage.save`, with two approaches:
   - **A:** `@TempDir` + change `user.dir` so `new Budgie()` uses `data/budgie.txt` under temp
   - **B:** package-private `Budgie(Storage)` injection (production change)
2. **ToT comparison** — agent returned pros/cons table; Joseph approved **approach A only**.
3. **Implement** — `BudgieTest.java` with six `getResponse_...` methods.

---

## Files touched

- `src/test/java/seedu/budgie/BudgieTest.java` (new)
- `docs/Reflections.md` (increment 15 stub appended)

No production code changes. No commit (unless Joseph asks).

---

## Test methods

| Method | Covers |
|---|---|
| `getResponse_addExpenseThenList_showsExactMessages` | Add → list messages + file line |
| `getResponse_deleteAfterAdd_persistsForNewBudgieInstance` | Delete + `new Budgie()` reload |
| `getResponse_summaryAfterAdds_includesNetLine` | Summary net through façade |
| `getResponse_budgetCommand_leavesBookAndFileUnchanged` | Unknown command, no save file |
| `getResponse_addExpense_writesSaveFile` | `modifiesData` → disk write |
| `getResponse_readOnlyCommands_doNotChangeSaveFile` | help / find / summary do not rewrite file |

---

## Approach A mechanics

- **Planned:** `@TempDir` + `System.setProperty("user.dir", tempDir)` so `new Budgie()` writes under temp.
- **Actual:** Java 17 caches the default directory at JVM start — changing `user.dir` does **not** repoint `Path.of("data/budgie.txt")`. Tests initially loaded Joseph’s real `data/budgie.txt` and failed.
- **Fix (still approach A — no production change):** Gradle `test.workingDir = build/test-run` in `build.gradle`; `@BeforeEach` deletes `data/budgie.txt` under that directory for isolation.

**Risk noted:** All tests now run with cwd `build/test-run` (harmless for other tests that inject `Storage(Path)` explicitly).

---

## What the model got right / wrong

- **Right:** Plan-first ToT avoided production edit; tests mirror existing command message strings from unit tests.
- **Wrong:** Assumed `user.dir` redirect works on Java 17 — it does not; caught only when `./gradlew check` failed.
- **Watch:** Overlaps with `StorageTest` / `ExpenseBookStorageIntegrationTest` at persistence layer — `BudgieTest` owns the façade + auto-save gate only.
- **Joseph should verify:** `./gradlew check` green; no reliance on repo-root `data/budgie.txt` during tests.
