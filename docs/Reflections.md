# Reflections on AI-assisted SE

This file is Joseph's reflection journal for CS3227 MP1. The agent may **append factual increment stubs**. It must not rewrite first-person sections.

Tools used: Cursor (agentic coding) + prompting.

## Guiding questions

Use these when expanding an increment into a deep dive:

- Why was the prompt formulated that way?
- What assumptions did the LLM make?
- What did it get wrong?
- How did I verify the result?
- How did the prompt evolve?
- When was prompting less effective than manual work?
- What engineering judgement was still required?
- What would I do differently next time?

## Deep dives (at least 3)

_To be expanded before submission from the strongest journal entries._

## Increment journal

<!-- Agent: append stubs below. Do not edit text written in first person. -->

### Increment 01 — Repo setup + greet / help / bye

Increment 01 — Repo setup + greet / help / bye
For this increment I asked Cursor to set up the CS3227 repo from the submission guideline and to implement only greet / help / bye. I deliberately did not ask it to build the whole budget tracker. The prompt was scoped that way because reflections are 25% of the grade: if I one-shot the product, I get no evidence of how prompts evolve, and the User Guide would list features that do not exist yet.

Assumptions the model made. I never named the app. Cursor chose Budgie and the package seedu.budgie, targeted Java 17 (my machine, not AddressBook’s Java 25), and treated unknown input as an error-plus-hint instead of Duke-style echo. Those are reasonable, but they were its product decisions, not requirements I wrote.

What it got wrong / what I had to watch. It drafted docs and logs claiming I still needed to run the app, and it was unsure the GitHub repo was public. I had already created the remote; the agent only filled the tree. Checkstyle also failed first (case indentation in Parser) before ./gradlew check went green. When I ran ./gradlew run myself, help and bye matched the User Guide, but Gradle’s progress bar printed between Budgie’s divider lines, so ./gradlew run is a messy way to demo the CLI. I did not type an unknown command in that session, so I have not personally confirmed that path yet.

How I verified. ./gradlew check (agent) plus my own ./gradlew run: greeting, help, bye, exit 0. I also confirmed the GitHub repo is public on master.

Prompt evolution. The first planning chats were “how do I maximise marks?” and “journal reflections feature-by-feature.” Only then did I say “set up the repo from this guideline.” That ordering helped: the agent already had “no Features.md”, “UG must match v0.1”, and “one increment”. A single “build me a budget tracker” prompt would have skipped that.

Prompting vs manual work. Generating Gradle, Checkstyle, CI, and the docs skeleton was faster with the agent. I still did git init and git remote add origin myself (I first typed git add origin … and it failed). Naming the product and locking “this is not a to-do list” are cheap to do by hand and expensive to undo later.

Judgement that stayed with me. I accepted the Ui / Parser / Command split so later features do not dump into one class. I accepted documenting only help and bye even though the real app will have expense. I still need to decide whether to keep the name Budgie; if not, rename before storage/JAR names spread.

Next time. Put the app name, Java version, and “do not implement later commands” in the first setup prompt. After the agent reports green tests, run the app myself immediately and try one negative input, not only help / bye. Prefer java -jar (or a quieter run task) for User Guide screenshots so Gradle progress does not look like part of the UI.

### Increment 02 — Add expense (in memory)

For this increment I asked Cursor to add only expense AMOUNT /CATEGORY DESCRIPTION in memory, and to update the User Guide, help text, tests, a log, and a reflection stub. I told it not to add income, list, delete, persistence, or GUI, and not to commit. The prompt was scoped that tightly so I would have a clear “what did the model assume?” story, and so the User Guide would not describe list before it exists.

Assumptions the model made. I gave the example expense 12.50 /food lunch but not the money type or error policy. It chose BigDecimal with at most two decimal places and a strictly positive amount. It also chose a one-word category after /, and BudgieException for a bad expense instead of “I don’t understand”. It also changed Command.execute to take an ExpenseBook, and it stopped lowercasing the whole line (increment 01 did that) so descriptions keep capitalisation. Those were reasonable, but they were its design, not a spec I wrote.

What it got wrong / what I had to watch. It did not try to implement the list feature, which is what I was worried about. I still had to confirm the User Guide tells testers that expenses are not listed and not saved. When I ran ./gradlew run, Gradle’s progress bar still mixed with the chatbot output, so typing was messy — that is the same CLI issue as increment 01, not a new expense bug.

How I verified. The agent ran ./gradlew check (parser and add-expense tests, Checkstyle). I ran the app myself: a bad command (expense without a description) showed the usage message; expense with /food beef and rice printed Added expense: $6.80 /food beef and rice; help listed expense; another add also worked. I did not wait until bye to prove data is gone, but the UG says the book is session-only.

Next time. Put one invalid example in the prompt (expense 12.50 /food with no description) so the UG, help, and tests cannot drift. After check is green, immediately try that invalid line myself — I did that this time, and I should keep doing it.

### Increment 03 — Add income (in memory)

**Status:** I asked Cursor to add only income with the same syntax as expense (income 2500 /salary August pay), and not list, delete, or save. I wanted one issue, one PR, and a User Guide that still says list is not a feature.

- **Feature / increment:** `income 2500 /salary August pay` with the same argument shape as expense. No list, delete, persistence, or GUI.
- **Prompts used:** Implement issue #2 only; same syntax as expense; do not add other features.
- **Assumptions the LLM made:**
  - Reuse the expense argument parser (`AMOUNT /CATEGORY DESCRIPTION`) and the same amount rules (`BigDecimal`, positive, max 2 d.p.).
  - Add a separate `Income` class rather than a shared `Transaction` type (avoids a large rename in this increment).
  - Display `2500` as `$2500.00`.
  - Invalid income uses `INCOME_USAGE`, not the expense usage string.
- **What to verify:** `./gradlew check`; manual `income 2500 /salary August pay`; `income 2500 /salary` (missing description); `help` lists income; `list` is still unknown.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still run the app.
- **Prompting vs hand work:** Copying expense into income is fast for the agent; choosing not to introduce `list` still has to be in the prompt.
- **Engineering judgement:** Shared `parseEntry` / `parseAmount` instead of duplicating regex. Kept two model classes (`Expense`, `Income`) for a smaller diff; a later increment can unify them before `list`.
- **Next time:** Decide up front whether income/expense should already be one `Transaction` type, so `list` does not have to merge two lists.

### Increment 04 — List transactions

**Status:** I asked Cursor to add only list, not delete or save. It added an Entry interface and one insertion-order list, so mixed adds stay in the order I typed them. Lines look like "1. [expense] $12.50 /food lunch". Empty book: No transactions yet. Add an expense or income first.



- **Feature / increment:** `list` shows expenses and incomes in the order they were added. No delete, persist, or GUI.
- **Prompts used:** Implement issue #3 only.
- **Assumptions the LLM made:**
  - `Entry` interface + one `ExpenseBook` list so mixed add order is preserved (expense then income then expense).
  - List lines look like `1. [expense] $12.50 /food lunch`.
  - Empty book: `No transactions yet. Add an expense or income first.`
  - Extra words after `list` are ignored (same as `help`).
- **What to verify:** `./gradlew check`; add mixed entries then `list`; `list` on a fresh run shows the empty message; `help` includes `list`.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still run the app.
- **Prompting vs hand work:** Numbered list formatting is easy for the agent. Choosing insertion-order vs “all expenses then all incomes” needed a design call (increment 03 already flagged this).
- **Engineering judgement:** Did not implement `delete` even though numbers look like indexes. Did not replace `Expense`/`Income` with one class — only added `Entry`.
- **Next time:** If delete is next, reuse these 1-based indexes in the UG so testers are not surprised.

### Increment 05 — Delete transaction

**Status:** I asked Cursor to add only delete, using the same numbers as list. Assumptions. delete 1 means the first row in list. Success looks like Deleted: [expense] $12.50 /food lunch. After that, remaining rows move up. delete / delete 0 show usage; a number that is too large shows a different message and does not remove anything.



- **Feature / increment:** `delete INDEX` using the same 1-based numbers as `list`. Remaining rows renumber. No persist or GUI.
- **Prompts used:** Implement delete only; not other commands.
- **Assumptions the LLM made:**
  - Index comes from `list` (start at 1).
  - Success: `Deleted: [expense] $12.50 /food lunch`
  - Bad format (`delete`, `delete 0`) uses `DELETE_USAGE`; index too large uses a different message and does not remove anything.
  - `Command.execute` may throw `BudgieException` (out-of-range delete).
- **What to verify:** `./gradlew check`; `list` then `delete 1` then `list` again; `delete 99` on a short list.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still run the app.
- **Engineering judgement:** Did not add persist. Did not use 0-based indexes. After delete, later items shift (like Duke).
- **Next time:** Persistence should save whatever is left after deletes, not a separate snapshot.

### Increment 06 — Persist to file

**Status:** draft — For this increment I asked Cursor to add file persistence only — no GUI and no new commands. I wanted it to save the live list after each add or delete, including after a delete, so a restart would not bring deleted rows back. The model chose data/budgie.txt and a Duke-style line format (E|amount|category|description). That is a product choice I should own, not something the assignment stated. I still need to run the app myself: add, delete, bye, start again, and list. Unit tests are not the same as seeing data/budgie.txt in the project folder. Missing-file first run should just look empty.


**Suggested first-person text (edit then keep):**

For this increment I asked Cursor to add file persistence only — no GUI and no new commands. I wanted it to save the live list after each add or delete, including after a delete, so a restart would not bring deleted rows back. The model chose `data/budgie.txt` and a Duke-style line format (`E|amount|category|description`). That is a product choice I should own, not something the assignment stated. I still need to run the app myself: add, delete, `bye`, start again, and `list`. Unit tests are not the same as seeing `data/budgie.txt` in the project folder. Missing-file first run should just look empty.

- **Feature / increment:** Auto-load/save `data/budgie.txt`. Save after `expense` / `income` / `delete`. Missing file starts empty. Invalid lines skipped. No GUI.
- **Prompts used:** Implement the next feature only; include a small reflection draft.
- **Assumptions the LLM made:**
  - Path `data/budgie.txt` relative to the working directory.
  - Line format `E|12.50|food|lunch` / `I|2500.00|salary|August pay`; last field may contain `|`.
  - Save immediately after mutating commands, including writing an empty file after deleting everything.
  - No `save` command; `help` text unchanged.
- **What to verify:** `./gradlew check`; add → delete → `bye` → run again → `list` shows remaining rows only; first run without a file is empty.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still run the restart path.
- **Engineering judgement:** Did not add GUI. Did not snapshot independently of deletes. Skipped corrupt lines instead of refusing to start.
- **Next time:** Remaining increment is extra error handling, then JavaFX.

### Increment 07 — Error messages

**Status:** draft — For this increment I asked Cursor to finish the error-handling bullet on the same persist branch: unknown command, missing amount, negative money, and unknown delete index. Some of those already existed as generic usage strings. What I wanted was four messages a peer tester can check against the User Guide. The model treated missing amount as expense with no amount token or a line that starts with /category, and it split negative amounts away from zero / too-many-decimals. That split is a design call I should own. I still need to type the four cases in the running app myself. GUI was not part of this increment.



**Suggested first-person text (edit then keep):**

For this increment I asked Cursor to finish the error-handling bullet on the same persist branch: unknown command, missing amount, negative money, and unknown delete index. Some of those already existed as generic usage strings. What I wanted was four messages a peer tester can check against the User Guide. The model treated missing amount as `expense` with no amount token or a line that starts with `/category`, and it split negative amounts away from zero / too-many-decimals. That split is a design call I should own. I still need to type the four cases in the running app myself. GUI was not part of this increment.

- **Feature / increment:** Distinct messages for unknown command, missing amount, negative amount, unknown `delete` index. No new commands and no GUI.
- **Prompts used:** Fix the errors bullet on the same branch.
- **Assumptions the LLM made:**
  - Missing amount: empty args, or first token starts with `/`.
  - Negative: `Amount cannot be negative.`
  - Unknown index stays `There is no transaction numbered N. Use list to see valid indexes.`
  - Unknown command stays the `Sorry, I don't understand ...` text.
- **What to verify:** `./gradlew check`; type `find food`, `expense /food lunch`, `expense -1 /food lunch`, `delete 99` and match the User Guide.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still type the four cases in the app.
- **Engineering judgement:** Did not add GUI. Did not turn zero amounts into the negative message. Did not treat `delete 0` as unknown index (still usage).
- **Next time:** JavaFX GUI.

### Increment 08 — JavaFX GUI

**Status:** draft — rewrite in first person the same day.

**Suggested first-person text (edit then keep):**

For this increment I asked Cursor to finish remaining P0: a JavaFX chat UI with a Launcher class, plus a fat JAR. I kept the same commands; the GUI is another front end on Budgie.getResponse, not a second parser. The model copied the SE-EDU Launcher pattern so `java -jar` does not die with “JavaFX runtime components are missing.” Chat bubbles and CSS are product choices I should own. I still need to open `./gradlew run` myself, type help / expense / bye, and confirm the window closes. There are no GUI tests by design.

- **Feature / increment:** JavaFX window via `Launcher` / `MainApp`. Same commands as the CLI. No P1 commands.
- **Prompts used:** Finish remaining P0; include commit message, PR description, and reflection draft.
- **Assumptions the LLM made:** `./gradlew run` opens GUI; `./gradlew runCli` keeps the old terminal; bubbles instead of Duke avatar PNGs.
- **What to verify:** `./gradlew check`; `./gradlew run` opens a window; type `help`, add, `list`, `bye` closes the window.
- **How it was verified (agent):** `./gradlew check` (and shadowJar if that run succeeded). Joseph should still open the GUI.
- **Engineering judgement:** Did not add budget/summary/find. Did not write automated GUI tests.
- **Next time:** Upload `budgie.jar` as a GitHub Release after merge.

### Increment 09 — Fat JAR / release packaging

**Status:** For this increment I asked Cursor to finish remaining P0: a JavaFX chat UI with a Launcher class, plus a fat JAR. I kept the same commands; the GUI is another front end on Budgie.getResponse, not a second parser. The model copied the SE-EDU Launcher pattern so java -jar does not die with “JavaFX runtime components are missing.

**Suggested first-person text (edit then keep):**

Packaging was bundled with the GUI increment because a GUI that testers cannot launch from a JAR is not P0. Shadow writes `build/libs/budgie.jar` with `Launcher` as the main class, and Gradle pulls JavaFX natives for Windows, Linux, and macOS (including Apple Silicon). Creating the actual GitHub Release is still a click I have to do after this is on `master` — attaching that JAR — because graders download from Releases. I should run `java -jar build/libs/budgie.jar` from a folder myself before I tag it.

- **Feature / increment:** `./gradlew shadowJar` → `budgie.jar`; UG/README match. GitHub Release is for Joseph after merge.
- **What to verify:** `java -jar build/libs/budgie.jar` opens the same GUI; data file is created next to the working directory.
- **How it was verified (agent):** intended `./gradlew shadowJar`. Joseph must run the JAR and create the Release.
- **Engineering judgement:** Followed AddressBook-style JavaFX classifiers instead of the OpenJFX Gradle plugin.
- **Next time:** Optional P1 only if v1.0 peer tests are clean.

### Increment 10 — UG / DG rewrite

**Status:** I asked Cursor to add only summary, not budget, dates, or edit. Because there is still no budget command, it does not show remaining vs a monthly cap. It shows total income, total expenses, net (income minus expenses), and a breakdown by category. Same-category amounts are added together. Empty summary uses the same message as empty list. 
**Suggested first-person text (edit then keep):**

I asked Cursor to expand the User Guide and Developer Guide using the same section layout as my CS2103 tP (EstateSearch): command summary table, notes on amounts (two decimal places), FAQ, glossary; DG architecture as UI / Logic / Model / Storage, a sequence for delete, then a requirements appendix (scope, user stories, use cases) and NFRs. I did not ask it to add product features. I still need to skim the UG samples against the running JAR so the table does not drift from `help`.

- **Feature / increment:** Docs only. v1.0 commands unchanged.
- **Prompts used:** Apply the EstateSearch UG/DG structure to Budgie.
- **Assumptions the LLM made:** Map AB3-style components onto `Budgie` / `ExpenseBook` / `Storage` / `MainWindow` rather than inventing `LogicManager`.
- **What to verify:** Command summary matches the app; tester step for `bye` closes the GUI; DG does not document unimplemented P1.
- **How it was verified (agent):** docs rewrite only.
- **Engineering judgement:** Kept v1.0 scope. Planned enhancements listed as not shipped.
- **Next time:** Code-quality refactor (shared Expense/Income) if there is spare time; otherwise polish reflections.

### Increment 11 — Find transactions

**Status:** I asked Cursor to add only find KEYWORD, not budget, summary, dates, or edit. It searches category and description as case-insensitive substrings, and amount by numeric value so 12.5 matches $12.50 but find 1 does not hit $12.50. Results keep list numbers so delete still makes sense. The unknown-command example in the UG is now budget 800. I still need to type find food in the GUI myself. The published v1.0 JAR does not have this until I rebuild.

**Suggested first-person text (edit then keep):**

I asked Cursor to add only `find KEYWORD`, not budget, summary, dates, or edit. It searches category and description as case-insensitive substrings, and amount by numeric value so `12.5` matches `$12.50` but `find 1` does not hit `$12.50`. Results keep `list` numbers so delete still makes sense. The unknown-command example in the UG is now `budget 800` because `find food` is a real command. I still need to type `find food` in the GUI myself. The published v1.0 JAR does not have this until I rebuild and maybe tag v1.1.

- **Feature / increment:** `find KEYWORD` only.
- **Prompts used:** Implement find only.
- **Assumptions the LLM made:** Original list indexes; amount equality not digit-contains; multi-word keyword is the rest of the line.
- **What to verify:** `./gradlew check`; `find food`, `find 12.50`, `find rent`, `find` usage; `budget 800` is unknown.
- **How it was verified (agent):** unit tests + intended `./gradlew check`.
- **Engineering judgement:** Did not add budget/summary. Did not number find results 1..n independently of list.
- **Next time:** Optional `summary` if find is clean.

### Increment 12 — Summary

**Status:** Agent draft (Joseph should rewrite this in first person).

**Suggested first-person text (edit then keep):**

I asked Cursor to add only `summary`, not budget, dates, or edit. Because there is still no `budget` command, it does not show remaining vs a monthly cap. It shows total income, total expenses, net (income minus expenses), and a breakdown by category. Same-category amounts are added together. Empty `summary` uses the same message as empty `list`. I still need to type `summary` in the GUI myself after a couple of adds. The published v1.0 JAR does not have this until I rebuild.

- **Feature / increment:** `summary` only.
- **Prompts used:** Implement the summary command now.
- **Assumptions the LLM made:** No remaining-budget line without `budget`; net = income − expenses; LinkedHashMap first-seen category order; extra words after `summary` ignored like `list`.
- **What to verify:** `./gradlew check`; `summary` after the UG’s two sample adds; empty `summary`; `budget 800` still unknown.
- **How it was verified (agent):** unit tests + intended `./gradlew check`.
- **Engineering judgement:** Did not invent a budget or remaining-cap. Did not skip to dates/`edit`.
- **Next time:** Optional `budget` if summary is clean, or polish reflections.




