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

**Status:** Agent draft (Joseph should rewrite this in first person).

- **Feature / increment:** `expense 12.50 /food lunch` stored in an in-memory `ExpenseBook`. No income, list, delete, persistence, or GUI.
- **Prompts used:** Scoped increment-02 prompt: exact command format; update UG + help; JUnit; `./gradlew check`; append log + reflection stub; do not overwrite increment-01; do not commit.
- **Assumptions the LLM made:**
  - Amount is `BigDecimal`, max 2 decimal places, must be strictly positive.
  - Category is a single token after `/`.
  - `Command.execute` should take `ExpenseBook` (small refactor of help/bye/unknown).
  - Parser lowercases only the command word so descriptions keep capitalisation (increment-01 lowercased the whole line).
  - Invalid expense format is `BudgieException`, not `UnknownCommand`.
- **What to verify:**
  - `./gradlew check`
  - Manual `expense 12.50 /food lunch` and a bad command such as `expense 12.50 /food`
  - Help text matches the User Guide
  - Expenses disappear after `bye` (no persistence — UG states this)
- **How it was verified (agent):** unit tests for parser/add; then `./gradlew check`. Joseph should still run the app.
- **Prompting vs hand work:** Wiring `Expense` / `Parser` / `AddExpenseCommand` is faster with the agent. Deciding “no list yet” and “don’t persist” is a human scope constraint; the model would otherwise add `list`.
- **Engineering judgement:** Keep `list`/`delete`/`income` out even though the book could support them. Usage error vs unknown command split. Money as `BigDecimal` rather than `double`.
- **Next time:** State whether extra-decimal amounts (`12.555`) must be rejected or rounded.


