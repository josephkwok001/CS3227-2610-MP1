
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


## Deep dives
### Note: increment logs for each LLM call are found after this section

### Deep dive 1 — Chaining prompt strategies into a pipeline (ToT → checklist → few-shot CoT plan → zero-shot CoT implement)

For the money-formatting refactor (Increment 13) I didn't just throw one prompt at Cursor and accept whatever came back. I deliberately chained four prompting techniques into a pipeline, each stage feeding its output into the next:

1. **Tree-of-Thought (ToT) exploration.** I asked the model to lay out multiple different ways to implement centralised money formatting (e.g. a `MoneyFormatter` utility class vs. formatting logic inside `Expense`/`Income` vs. a `Formattable` interface), and to also generate a checklist of things a correct refactor must preserve (existing `find`/`summary` output, `BigDecimal` rounding, no behaviour change).
2. **Multi-shot Chain-of-Thought (CoT), plan-only.** I pasted the ToT output (branches + checklist) into a fresh prompt with a few worked planning examples, and explicitly told it to **only plan**, not write code yet. This forced it to commit to one branch and produce a numbered implementation plan before touching any file.
3. **Zero-shot CoT implementation.** I then pasted that numbered plan into a third, fresh chat and used the zero-shot COT prompting strategy "let's think step by step" to implement it. However I scoped it strictly to *implementing the plan as written* rather than re-deciding the design.

**Why I formulated it this way.** Doing design, planning, and implementation in one giant prompt tends to produce code where the "design" is implicit and hard to interrogate. If something goes wrong, I can't tell whether it was a design mistake or an implementation slip. Splitting it into three separate stages meant each stage had a narrow job, and I could inspect and approve the output of each stage independently before it became irreversible in code. However, one thing to note was that this strategy of prompting took quite a while, and pasting the output to a new chat was a hassle. Perhaps the tradeoff of this is that it is time consuming and requires multiple steps.

**What the LLM assumed / got wrong.** At the ToT stage it assumed I wanted backward-compatible output (I hadn't said this explicitly, but it inferred it from the existing tests) — that assumption turned out to be exactly right and probably saved me a round of rework. At the planning stage it initially proposed *merging* `Expense` and `Income` into one type as part of the refactor, which I explicitly rejected before moving to implementation, since that's a much bigger change than "fix formatting" and would have blown up the diff.

**How I verified.** `./gradlew check` stayed green, and because the plan explicitly said "no behaviour change," I could diff the test output before/after rather than re-reviewing everything from scratch.

**Engineering judgement that stayed with me.** The pipeline doesn't remove the need to make the actual design call (standalone class vs. shared type), it just makes *when* I make that call more deliberate. I still had to reject the Expense/Income merge myself; the pipeline surfaced it as an explicit decision point rather than letting it get silently folded into the implementation.

**Next time.** I'd keep the ToT-checklist artifact around as a lightweight design doc rather than discarding it. It would have been useful evidence in this very reflection, and it's the kind of thing that's easy to lose once you move to the next chat.

### Deep dive 2 — Writing an `AGENTS.md` file to constrain the agent's own behaviour

In Increment 01 I asked Cursor to generate AGENTS.md and a matching Cursor rule (.cursor/rules/mp1-ai-workflow.mdc) so every session would inherit the same standing instructions: read Reflections.md first, implement one increment at a time, keep the User Guide accurate, write logs, append reflection stubs without touching my first-person text, and never add a Features.md. The header note at the top of this file, "The agent may append factual increment stubs. It must not rewrite first-person sections", came from that setup.

**Why I did this.** Even with those files in place from the start, I still found myself re-stating the same constraints in feature prompts across increments 01–07: "only this increment," "don't touch reflections I've already written," "don't commit unless I ask." AGENTS.md absorbed the workflow rules, but it did not stop me from having to narrow scope in every session, and it did not stop the agent from making product decisions I should have owned.

**What was interesting about the output.** The first draft of AGENTS.md mixed two kinds of content. Useful but generic: SE-EDU Java style, Checkstyle, commit-message conventions. More valuable for this project: increment order, no to-do clone, append-only reflections, logs after each session. The gap showed up clearly in Increment 06 (persist): the agent implemented data/budgie.txt and a Duke-style line format (E|amount|category|description) without me treating path or format as an explicit design choice first. That lesson landed in my increment reflection and in how I worded later prompts ("flag design choices before implementing"), but it never became its own line in AGENTS.md.

**Engineering judgement.** A constraint file is only as good as my enforcement of it. Cursor does not guarantee compliance, so in later increments I still spot-checked the first response against AGENTS.md (scope, UG sync, reflection append-only) rather than assuming the agent had read and obeyed everything. The file reduced how often I had to explain the full workflow from scratch, it did not remove my responsibility to review diffs and catch silent design calls.

**What I'd do differently.** I would keep creating AGENTS.md in Increment 01, but I would update it immediately after Increment 06 with an explicit rule such as: do not choose persistence path or file format without flagging it as my decision. I spent several increments re-learning the same "don't overstep scope / don't silently decide architecture" lesson in prompts and reflections when a one-line addition to the standing file might have prevented the next occurrence.

### Deep dive 3 — Tree-of-Thought for the GUI polish, picking B then course-correcting to C (Increment 17)

For the JavaFX GUI polish I used a ToT-style prompt asking Cursor to propose three distinct options rather than one: (A) CSS-only tweaks to the existing chat bubbles, (B) wider bubbles with better spacing, (C) a fuller `BorderPane` + `TextFlow` layout with a title bar and a dedicated input strip.

**Why I formulated it that way.** GUI polish is subjective and hard to specify precisely up front. I genuinely didn't know what was considered a good design until I could compare options side by side, so a single "make the GUI nicer" prompt would have given the model too much unconstrained freedom and me too little to react to. ToT let me evaluate concrete, bounded alternatives instead.

**What happened.** I approved and implemented Option B first, since not only did Cursor recommend I pick it, it also looked like the smallest and safest change. After running it and actually using the chat window, it didn't fix what I actually needed. The chat still felt cramped, the output text of the help feature was still messy. I then went back to the same ToT output and asked for Option C instead, which added the title bar, a distinct bottom input strip, and `TextFlow` bubbles. This version addressed the actual usability issue.

**What did it get wrong / what did I get wrong?** In hindsight, the LLM didn't get anything wrong here. *I* picked the option based on how it read in the plan description rather than how it would feel to actually use, which is exactly the kind of thing a written description under-specifies. The model's B and C implementations both matched their descriptions faithfully; the mismatch was between my mental model and what I actually needed.

**How I verified.** `./gradlew check` stayed green for both attempts (no GUI tests exist by design), so verification here was manual: running the app and using the chat window myself, which is what actually caught B's shortfall — a purely automated check would not have.

**Engineering judgement / what I'd do differently.** I should have picked one option and fully committed to evaluating it interactively *before* asking for the next, rather than assuming I could judge from the plan text alone. Especially since this was a section on the UI, where the LLM may not be able to fully capture what I envision. More generally, for any UI/UX decision, ToT's value comes from generating options, but *choosing* the right option still requires trying it, not just reading about it. Next time I'd budget for "try it, then maybe switch" as part of the ToT workflow rather than treating my first pick as final.

### Deep dive 4 — AI-assisted unit test generation surfaced a real gap, but also duplicated existing coverage (Increment 14)

For Increment 14 I followed the lecture’s structured approach to AI-assisted unit testing, where I defined a specific scope/function. However, I also shaped the prompts around the starter prompt introduced in lecture and introduced some rules that I wanted to try. For example act like an autonomous software engineer, optimise for correctness over speed, conform to existing conventions, search before duplicating, and only claim completion after reconciling the plan.

**Why I formulated the prompts that way.** I prepended a shared rules block to every prompt. I told it tests only, no production changes unless a real bug appears. I also told it to frequently run ./gradlew check and explain each case in a scenario table linked to the User Guide. Then I sent four separate, narrowly scoped prompts: EntryMatcher, expanded MoneyFormatterTest, ExpenseBook.delete, and consolidated Parser amount validation, each ending with “STOP after one test class + explanation.”

Autonomy and persistence (bounded). Default agent instructions say something like “once the user gives a direction, proactively gather context, plan, implement, test, and refine without waiting at every step.” I wanted that autonomy for implementation, but not for scope. One broad “write more tests” prompt would let the agent roam across the whole suite and make review painful. Splitting into four prompts gave me bounded autonomy: the agent could still read files, generate tests, and iterate on Checkstyle within one class, but I could revert or reject one chunk without untangling four concerns. Prompt 1 even said “Do not duplicate scenarios already covered in FindCommandTest unless they test EntryMatcher directly” — an attempt to steer the agent toward DRY / search first before adding new @Test methods.


**What the LLM got right.** It found a genuine gap I had missed: income amounts with more than two decimal places (e.g. 12.555) were not covered anywhere, even though the User Guide says they should be rejected. That came from building a full scenario table for Parser amount validation — a good outcome of letting it plan cases systematically rather than me listing every edge case by hand.

**What it got wrong.** Several EntryMatcher and ExpenseBook tests duplicated scenarios already in FindCommandTest and DeleteCommandTest. The agent had been told to search for overlap, but it did not treat “read existing test files and skip covered scenarios” as part of search first, it generated technically correct but redundant tests. On Prompt 4, its first pass failed Checkstyle despite the shared block saying to run ./gradlew check. This was actually the opposite of what I wanted it to do, which was to “optimise for correctness and reliability over speed” and “conform to codebase conventions”. ./gradlew check only went green after I asked it to fix Checkstyle. It had implemented tests but had not reconciled “green build” as a stated intention before ending the turn.


**How I verified.** I ran ./gradlew check myself after each prompt, skimmed new tests against the existing suite for duplication (nothing automated flags redundant-but-passing tests), and used the agent’s scenario tables to decide which cases were net-new vs already covered at the command layer.

**Engineering judgement that stayed with me.** This is the clearest example of the lecture’s “review, refine, and consolidate” step mattering. Accepting every generated @Test would bloat the suite without improving coverage. I also learned that autonomy needs explicit guardrails: “search existing tests first,” “run check before saying done,” and “mark blocked items (e.g. Checkstyle) instead of implying completion.” I kept some duplicate model-layer tests for refactor safety, but I am not fully happy with that trade-off.


**Next time.** I would add explicit prompt lines drawn from those agent rules: 
- (1) DRY: “List existing tests that cover each scenario; only add a test if no prior art exists or this layer must be tested in isolation.” 
- (2) Closure: “Do not end until ./gradlew check is green; if Checkstyle fails, fix and re-run before summarising.” 
- (3) Bounded autonomy: keep one-class-per-prompt, but paste paths to FindCommandTest / DeleteCommandTest so search-first is concrete, not implied.


## Increment journal

<!-- Agent: append stubs below. Do not edit text written in first person. -->

### Increment 01: Repo setup + greet / help / bye

For this increment I asked Cursor to set up the CS3227 repo from the submission guideline and to implement only greet / help / bye. I deliberately did not ask it to build the whole budget tracker. The prompt was scoped that way because reflections are 25% of the grade: if I one-shot the product, I get no evidence of how prompts evolve, and the User Guide would list features that do not exist yet.

Assumptions the model made. I never named the app. Cursor chose Budgie and the package seedu.budgie, targeted Java 17 (my machine, not AddressBook’s Java 25), and treated unknown input as an error plus hint instead of Duke style echo. Those are reasonable, but they were its product decisions, not requirements I wrote.

What it got wrong / what I had to watch. It drafted docs and logs claiming I still needed to run the app, and it was unsure the GitHub repo was public. I had already created the remote. The agent only filled the tree. Checkstyle also failed first (case indentation in Parser) before ./gradlew check went green. When I ran ./gradlew run myself, help and bye matched the User Guide, but Gradle’s progress bar printed between Budgie’s divider lines, so ./gradlew run is a messy way to demo the CLI. I did not type an unknown command in that session, so I have not personally confirmed that path yet.

How I verified. ./gradlew check (agent) plus my own ./gradlew run: greeting, help, bye, exit 0. I also confirmed the GitHub repo is public on master.

Prompt evolution. The first planning chats were “how do I maximise marks?” and “journal reflections feature by feature.” Only then did I say “set up the repo from this guideline.” That ordering helped: the agent already had “no Features.md”, “UG must match v0.1”, and “one increment”. A single “build me a budget tracker” prompt would have skipped that.

Prompting vs manual work. Generating Gradle, Checkstyle, CI, and the docs skeleton was faster with the agent. I still did git init and git remote add origin myself (I first typed git add origin … and it failed). Naming the product and locking “this is not a to do list” are cheap to do by hand and expensive to undo later.

Judgement that stayed with me. I accepted the Ui / Parser / Command split so later features do not dump into one class. I accepted documenting only help and bye even though the real app will have expense. I still need to decide whether to keep the name Budgie. If not, rename before storage/JAR names spread.

Next time. Put the app name, Java version, and “do not implement later commands” in the first setup prompt. After the agent reports green tests, run the app myself immediately and try one negative input, not only help / bye. Prefer java -jar (or a quieter run task) for User Guide screenshots so Gradle progress does not look like part of the UI.

### Increment 02: Add expense (in memory)

For this increment I asked Cursor to add only expense AMOUNT /CATEGORY DESCRIPTION in memory, and to update the User Guide, help text, tests, a log, and a reflection stub. I told it not to add income, list, delete, persistence, or GUI, and not to commit. The prompt was scoped that tightly so I would have a clear “what did the model assume?” story, and so the User Guide would not describe list before it exists.

Assumptions the model made. I gave the example expense 12.50 /food lunch but not the money type or error policy. It chose BigDecimal with at most two decimal places and a strictly positive amount. It also chose a one-word category after /, and BudgieException for a bad expense instead of “I don’t understand”. It also changed Command.execute to take an ExpenseBook, and it stopped lowercasing the whole line (increment 01 did that) so descriptions keep capitalisation. Those were reasonable, but they were its design, not a spec I wrote.

What it got wrong / what I had to watch. It did not try to implement the list feature, which is what I was worried about. I still had to confirm the User Guide tells testers that expenses are not listed and not saved. When I ran ./gradlew run, Gradle’s progress bar still mixed with the chatbot output, so typing was messy. That is the same CLI issue as increment 01, not a new expense bug.

How I verified. The agent ran ./gradlew check (parser and add expense tests, Checkstyle). I ran the app myself: a bad command (expense without a description) showed the usage message. Expense with /food beef and rice printed Added expense: $6.80 /food beef and rice. Help listed expense. Another add also worked. I did not wait until bye to prove data is gone, but the UG says the book is session only.

Next time. Put one invalid example in the prompt (expense 12.50 /food with no description) so the UG, help, and tests cannot drift. After check is green, immediately try that invalid line myself. I did that this time, and I should keep doing it.

### Increment 03: Add income (in memory)

**Status:** I asked Cursor to add only income with the same syntax as expense (income 2500 /salary August pay), and not list, delete, or save. I wanted one issue, one PR, and a User Guide that still says list is not a feature.

- **Feature / increment:** `income 2500 /salary August pay` with the same argument shape as expense. No list, delete, persistence, or GUI.
- **Prompts used:** Implement issue #2 only. Same syntax as expense. Do not add other features.
- **Assumptions the LLM made:**
  - Reuse the expense argument parser (`AMOUNT /CATEGORY DESCRIPTION`) and the same amount rules (`BigDecimal`, positive, max 2 d.p.).
  - Add a separate `Income` class rather than a shared `Transaction` type (avoids a large rename in this increment).
  - Display `2500` as `$2500.00`.
  - Invalid income uses `INCOME_USAGE`, not the expense usage string.
- **What to verify:** `./gradlew check`. Manual `income 2500 /salary August pay`. `income 2500 /salary` (missing description). `help` lists income. `list` is still unknown.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still run the app.
- **Prompting vs hand work:** Copying expense into income is fast for the agent. Choosing not to introduce `list` still has to be in the prompt.
- **Engineering judgement:** Shared `parseEntry` / `parseAmount` instead of duplicating regex. Kept two model classes (`Expense`, `Income`) for a smaller diff. A later increment can unify them before `list`.
- **Next time:** Decide up front whether income/expense should already be one `Transaction` type, so `list` does not have to merge two lists.

### Increment 04: List transactions

**Status:** I asked Cursor to add only list, not delete or save. It added an Entry interface and one insertion order list, so mixed adds stay in the order I typed them. Lines look like "1. [expense] $12.50 /food lunch". Empty book: No transactions yet. Add an expense or income first.



- **Feature / increment:** `list` shows expenses and incomes in the order they were added. No delete, persist, or GUI.
- **Prompts used:** Implement issue #3 only.
- **Assumptions the LLM made:**
  - `Entry` interface + one `ExpenseBook` list so mixed add order is preserved (expense then income then expense).
  - List lines look like `1. [expense] $12.50 /food lunch`.
  - Empty book: `No transactions yet. Add an expense or income first.`
  - Extra words after `list` are ignored (same as `help`).
- **What to verify:** `./gradlew check`. Add mixed entries then `list`. `list` on a fresh run shows the empty message. `help` includes `list`.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still run the app.
- **Prompting vs hand work:** Numbered list formatting is easy for the agent. Choosing insertion order vs “all expenses then all incomes” needed a design call (increment 03 already flagged this).
- **Engineering judgement:** Did not implement `delete` even though numbers look like indexes. Did not replace `Expense`/`Income` with one class, only added `Entry`.
- **Next time:** If delete is next, reuse these 1-based indexes in the UG so testers are not surprised.

### Increment 05: Delete transaction

**Status:** I asked Cursor to add only delete, using the same numbers as list. Assumptions. delete 1 means the first row in list. Success looks like Deleted: [expense] $12.50 /food lunch. After that, remaining rows move up. delete / delete 0 show usage. A number that is too large shows a different message and does not remove anything.


- **Feature / increment:** `delete INDEX` using the same 1-based numbers as `list`. Remaining rows renumber. No persist or GUI.
- **Prompts used:** Implement delete only. Not other commands.
- **Assumptions the LLM made:**
  - Index comes from `list` (start at 1).
  - Success: `Deleted: [expense] $12.50 /food lunch`
  - Bad format (`delete`, `delete 0`) uses `DELETE_USAGE`. Index too large uses a different message and does not remove anything.
  - `Command.execute` may throw `BudgieException` (out of range delete).
- **What to verify:** `./gradlew check`. `list` then `delete 1` then `list` again. `delete 99` on a short list.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still run the app.
- **Engineering judgement:** Did not add persist. Did not use 0-based indexes. After delete, later items shift (like Duke).
- **Next time:** Persistence should save whatever is left after deletes, not a separate snapshot.

### Increment 06: Persist to file

For this increment I asked Cursor to add file persistence only, no GUI and no new commands. I wanted it to save the live list after each add or delete, including after a delete, so a restart would not bring deleted rows back. The model chose `data/budgie.txt` and a Duke style line format (`E|amount|category|description`). That is a product choice I should own, not something the assignment stated. I still need to run the app myself: add, delete, `bye`, start again, and `list`. Unit tests are not the same as seeing `data/budgie.txt` in the project folder. Missing file first run should just look empty.

- **Feature / increment:** Auto load/save `data/budgie.txt`. Save after `expense` / `income` / `delete`. Missing file starts empty. Invalid lines skipped. No GUI.
- **Prompts used:** Implement the next feature only. Include a small reflection draft.
- **Assumptions the LLM made:**
  - Path `data/budgie.txt` relative to the working directory.
  - Line format `E|12.50|food|lunch` / `I|2500.00|salary|August pay`. Last field may contain `|`.
  - Save immediately after mutating commands, including writing an empty file after deleting everything.
  - No `save` command. `help` text unchanged.
- **What to verify:** `./gradlew check`. Add → delete → `bye` → run again → `list` shows remaining rows only. First run without a file is empty.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still run the restart path.
- **Engineering judgement:** Did not add GUI. Did not snapshot independently of deletes. Skipped corrupt lines instead of refusing to start.
- **Next time:** Remaining increment is extra error handling, then JavaFX.

### Increment 07: Error messages

For this increment I asked Cursor to finish the error handling bullet on the same persist branch: unknown command, missing amount, negative money, and unknown delete index. Some of those already existed as generic usage strings. What I wanted was four messages a peer tester can check against the User Guide. The model treated missing amount as `expense` with no amount token or a line that starts with `/category`, and it split negative amounts away from zero / too many decimals. That split is a design call I should own. I still need to type the four cases in the running app myself. GUI was not part of this increment.

- **Feature / increment:** Distinct messages for unknown command, missing amount, negative amount, unknown `delete` index. No new commands and no GUI.
- **Prompts used:** Fix the errors bullet on the same branch.
- **Assumptions the LLM made:**
  - Missing amount: empty args, or first token starts with `/`.
  - Negative: `Amount cannot be negative.`
  - Unknown index stays `There is no transaction numbered N. Use list to see valid indexes.`
  - Unknown command stays the `Sorry, I don't understand ...` text.
- **What to verify:** `./gradlew check`. Type `find food`, `expense /food lunch`, `expense -1 /food lunch`, `delete 99` and match the User Guide.
- **How it was verified (agent):** unit tests + `./gradlew check`. Joseph should still type the four cases in the app.
- **Engineering judgement:** Did not add GUI. Did not turn zero amounts into the negative message. Did not treat `delete 0` as unknown index (still usage).
- **Next time:** JavaFX GUI.

### Increment 08: JavaFX GUI

For this increment I asked Cursor to finish remaining P0: a JavaFX chat UI with a Launcher class, plus a fat JAR. I kept the same commands. The GUI is another front end on Budgie.getResponse, not a second parser. The model copied the SE-EDU Launcher pattern so `java -jar` does not die with “JavaFX runtime components are missing.” Chat bubbles and CSS are product choices I should own. I still need to open `./gradlew run` myself, type help / expense / bye, and confirm the window closes. There are no GUI tests by design.

- **Feature / increment:** JavaFX window via `Launcher` / `MainApp`. Same commands as the CLI. No P1 commands.
- **Prompts used:** Finish remaining P0. Include commit message, PR description, and reflection draft.
- **Assumptions the LLM made:** `./gradlew run` opens GUI. `./gradlew runCli` keeps the old terminal. Bubbles instead of Duke avatar PNGs.
- **What to verify:** `./gradlew check`. `./gradlew run` opens a window. Type `help`, add, `list`, `bye` closes the window.
- **How it was verified (agent):** `./gradlew check` (and shadowJar if that run succeeded). Joseph should still open the GUI.
- **Engineering judgement:** Did not add budget/summary/find. Did not write automated GUI tests.
- **Next time:** Upload `budgie.jar` as a GitHub Release after merge.

### Increment 09: Fat JAR / release packaging

Packaging was bundled with the GUI increment because a GUI that testers cannot launch from a JAR is not P0. Shadow writes `build/libs/budgie.jar` with `Launcher` as the main class, and Gradle pulls JavaFX natives for Windows, Linux, and macOS (including Apple Silicon). Creating the actual GitHub Release is still a click I have to do after this is on `master`, attaching that JAR, because graders download from Releases. I should run `java -jar build/libs/budgie.jar` from a folder myself before I tag it.

- **Feature / increment:** `./gradlew shadowJar` → `budgie.jar`. UG/README match. GitHub Release is for Joseph after merge.
- **What to verify:** `java -jar build/libs/budgie.jar` opens the same GUI. Data file is created next to the working directory.
- **How it was verified (agent):** intended `./gradlew shadowJar`. Joseph must run the JAR and create the Release.
- **Engineering judgement:** Followed AddressBook style JavaFX classifiers instead of the OpenJFX Gradle plugin.
- **Next time:** Optional P1 only if v1.0 peer tests are clean.

### Increment 10: UG / DG rewrite

I asked Cursor to expand the User Guide and Developer Guide using the same section layout as my CS2103 tP (EstateSearch): command summary table, notes on amounts (two decimal places), FAQ, glossary. DG architecture as UI / Logic / Model / Storage, a sequence for delete, then a requirements appendix (scope, user stories, use cases) and NFRs. I did not ask it to add product features. I still need to skim the UG samples against the running JAR so the table does not drift from `help`.

- **Feature / increment:** Docs only. v1.0 commands unchanged.
- **Prompts used:** Apply the EstateSearch UG/DG structure to Budgie.
- **Assumptions the LLM made:** Map AB3 style components onto `Budgie` / `ExpenseBook` / `Storage` / `MainWindow` rather than inventing `LogicManager`.
- **What to verify:** Command summary matches the app. Tester step for `bye` closes the GUI. DG does not document unimplemented P1.
- **How it was verified (agent):** docs rewrite only.
- **Engineering judgement:** Kept v1.0 scope. Planned enhancements listed as not shipped.
- **Next time:** Code quality refactor (shared Expense/Income) if there is spare time. Otherwise polish reflections.

### Increment 11: Find transactions

I asked Cursor to add only `find KEYWORD`, not budget, summary, dates, or edit. It searches category and description as case insensitive substrings, and amount by numeric value so `12.5` matches `$12.50` but `find 1` does not hit `$12.50`. Results keep `list` numbers so delete still makes sense. The unknown command example in the UG is now `budget 800` because `find food` is a real command. I still need to type `find food` in the GUI myself. The published v1.0 JAR does not have this until I rebuild and maybe tag v1.1.

- **Feature / increment:** `find KEYWORD` only.
- **Prompts used:** Implement find only.
- **Assumptions the LLM made:** Original list indexes. Amount equality not digit contains. Multi word keyword is the rest of the line.
- **What to verify:** `./gradlew check`. `find food`, `find 12.50`, `find rent`, `find` usage. `budget 800` is unknown.
- **How it was verified (agent):** unit tests + intended `./gradlew check`.
- **Engineering judgement:** Did not add budget/summary. Did not number find results 1..n independently of list.
- **Next time:** Optional `summary` if find is clean.

### Increment 12: Summary

**Status:** I asked Cursor to add only `summary`, not budget, dates, or edit. Because there is still no `budget` command, it does not show remaining vs a monthly cap. It shows total income, total expenses, net (income minus expenses), and a breakdown by category. Same category amounts are added together. Empty `summary` uses the same message as empty `list`. I still need to type `summary` in the GUI myself after a couple of adds. The published v1.0 JAR does not have this until I rebuild.

- **Feature / increment:** `summary` only.
- **Prompts used:** Implement the summary command now.
- **Assumptions the LLM made:** No remaining budget line without `budget`. Net = income − expenses. LinkedHashMap first seen category order. Extra words after `summary` ignored like `list`.
- **What to verify:** `./gradlew check`. `summary` after the UG’s two sample adds. Empty `summary`. `budget 800` still unknown.
- **How it was verified (agent):** unit tests + intended `./gradlew check`.
- **Engineering judgement:** Did not invent a budget or remaining cap. Did not skip to dates/`edit`.
- **Next time:** Optional `budget` if summary is clean, or polish reflections.

### Increment 13: Refactor money formatting (ToT pipeline)

**Status:** I ran a three-stage prompt pipeline on a pure refactor: ToT for design (chose `MoneyFormatter` in `model`), few shot CoT for a numbered plan, then zero shot implement from that plan only. It centralised `12.50` / `$12.50` / `-$15.50` formatting without merging `Expense` and `Income`. `./gradlew check` stayed green and I did not change the User Guide because outputs should be identical. I still need to manually try `find 1`, `find 12.5`, and a negative `summary` net in the GUI or JAR.

- **Feature / increment:** Refactor only, `MoneyFormatter`. No new commands.
- **Prompts used:** ToT design → few shot plan → plan conditioned implement (Chat 3).
- **Assumptions the LLM made:** Three methods (`formatPlain`, `formatDisplay`, `formatSignedDisplay`). `EntryMatcher` keeps `BigDecimal.compareTo` for amount keywords. Parser/Storage deferred.
- **What to verify:** `./gradlew check`. `find 12.5` / `find 1`. `summary` with expenses > income shows `-$x.xx`. Storage round trip unchanged.
- **How it was verified (agent):** 52 tests + Checkstyle green. Existing command tests unchanged.
- **Engineering judgement:** Did not merge Expense/Income. Did not bundle Parser `parseAmount` sharing.
- **Next time:** Optional `budget` or Parser/Storage amount parsing refactor. Expand reflection deep dive on this pipeline.

### Increment 14: AI assisted unit tests

**Status:** After the refactor pipeline (log 13), I tried the lecture’s AI assisted unit testing style: a shared rules block, then four focused prompts, `EntryMatcher`, expanded `MoneyFormatterTest`, `ExpenseBook.delete`, and consolidated `Parser` amount validation. I asked for scenario tables, UG linked cases, and “tests only, no production code.” The model produced sensible tests quickly and even found a gap (income `12.555` was not covered before). It also duplicated some scenarios already in `FindCommandTest` / `DeleteCommandTest`, and the Parser consolidation failed Checkstyle on the first run (method names, line length, `SeparatorWrap`) until it refactored `assertThrows` lines. I still need to decide whether to keep overlapping model tests or trim them. The prompts are in `logs/14-ai-unit-tests.md`.

- **Feature / increment:** Unit tests only, no new commands.
- **Prompts used:** Shared test block + four lecture style prompts (EntryMatcher, MoneyFormatter, ExpenseBook, Parser amount validation). See `logs/14-ai-unit-tests.md`.
- **How I prompted:** Explicit scope (“unit only”, no Parser in EntryMatcher tests). Scenario template tables. Link to UG (`find 1`, Errors section). “consolidate, remove duplication”. “explain test design table”.
- **LLM response, what worked:** Fast coverage of UG edge cases. New `EntryMatcherTest` / `ExpenseBookTest`. Income too many decimals gap filled. Parser tests merged into two methods.
- **LLM response, what to watch:** Overlap with existing command tests. First Parser edit broke Checkstyle. Minor duplicate assertions in `MoneyFormatterTest`. Did not run integration/Budgie tests (out of scope).
- **How it was verified (agent):** `./gradlew check` green after Checkstyle fixes.
- **Engineering judgement:** I should review and delete weak/duplicate tests myself, the lecture says review and consolidate, not accept every generated `@Test`.
- **Next time:** One “test design only” prompt before code. Optional `EntryMatcherTest` only if I keep model layer tests separate from `FindCommandTest`.

### Increment 15: Budgie integration tests (ToT)

**Status:** Agent draft (Joseph should rewrite this in first person).

**Suggested first-person text (edit then keep):**

After unit tests (increment 14), I wanted integration tests for `Budgie.getResponse(String)` that cross Parser, Command, ExpenseBook, and `Storage.save` without mocking the façade. I used a Tree of Thought prompt first: approach A (`@TempDir` + default `data/budgie.txt` path, no production changes) vs approach B (inject `Storage` via a test constructor). I approved A only. The first implementation failed because `System.setProperty("user.dir")` does not repoint `Path.of("data/budgie.txt")` on Java 17. The fix stayed within approach A: Gradle `test.workingDir = build/test-run` plus `@BeforeEach` cleanup of `data/budgie.txt`. `./gradlew check` went green with 79 tests. I still need to skim `BudgieTest` and decide whether overlap with `ExpenseBookStorageIntegrationTest` is acceptable. Prompts and the ToT table are in `logs/15-budgie-integration-tests.md`.

- **Feature / increment:** Integration tests for `Budgie.getResponse` only. No production code changes.
- **Prompts used:** ToT plan (A vs B) → approve A → implement scenarios. See `logs/15-budgie-integration-tests.md`.
- **Assumptions the LLM made:** Default `Storage` path. Six scenarios: add/list, delete + reload, summary net line, unknown `budget 800`, expense writes file, read-only commands skip save.
- **What to verify:** `./gradlew check` (79 tests). `BudgieTest` does not read Joseph’s real save file. Global `test.workingDir` acceptable for whole suite.
- **How it was verified (agent):** `./gradlew check` green after Gradle workingDir fix.
- **Engineering judgement:** Kept approach A after user.dir failure. Did not add `Budgie(Storage)` without approval. Some overlap with storage integration tests is intentional (façade vs direct storage).
- **Next time:** Put “Java 17 caches default directory” in the ToT table up front. Consider B if parallel test isolation becomes a problem.

### Increment 16: release/ folder with fat JAR

**Status:** The course asks for a `release/` folder with the latest fat JAR. I only had `./gradlew shadowJar` writing to gitignored `build/libs/`. I asked Cursor to add a `release` Gradle task that copies `budgie.jar` into `release/` after shadowJar, and to update the README/UG/DG. `./gradlew release` produced a ~10 MB JAR with JavaFX. I still need to run `java -jar release/budgie.jar` myself, commit `release/budgie.jar` for submission if required, and optionally tag a v1.2 GitHub Release.

- **Feature / increment:** `release/budgie.jar` via `./gradlew release`. Docs updated.
- **What to verify:** `./gradlew release`. `java -jar release/budgie.jar`. GUI + find/summary. Commit JAR for graders.
- **How it was verified (agent):** `./gradlew release` + `./gradlew check`.
- **Next time:** Re-run `./gradlew release` before any submission or GitHub Release tag.

### Increment 17: GUI polish and help formatting

**Status:** I used a ToT prompt to compare three GUI polish levels (CSS only vs wider bubbles vs BorderPane + TextFlow). I approved Option B first, then asked to try Option C so I could see the difference. C added a title bar, bottom input strip, TextFlow bubbles, and hiding repeated “Budgie” / “You” labels. The help list was still hard to read, so I reformatted only `HelpCommand.MESSAGE` to use `command` then `description` lines (with an em dash between them in the app) and styled command words in the GUI. I did not change other commands or add buttons. I still need to run `./gradlew release` so `release/budgie.jar` matches the new UI, and walk the User Guide tester path myself. Prompts and files are in `logs/17-gui-polish-and-help-format.md`.

- **Feature / increment:** GUI polish + `help` text formatting only.
- **Prompts used:** ToT (A/B/C) → approve B → implement C → help format fix. See `logs/17-gui-polish-and-help-format.md`.
- **Assumptions the LLM made:** AddressBook style chat kept. No command buttons. Help em dash format. Small `MainApp` tweak for BorderPane.
- **What to verify:** `./gradlew check`. `./gradlew run` + JAR: `help`, `list`, `summary`, `bye`. UG help sample matches app. Rebuild `release/budgie.jar`.
- **How it was verified (agent):** `./gradlew check` green. No GUI tests.
- **Engineering judgement:** Did not add WebView, avatars, or new commands. Help text change requires UG sync.
- **Next time:** Pick one polish option before implementing both B and C. Good ToT deep dive material.


