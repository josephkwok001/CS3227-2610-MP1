# Log 01 — Repo setup + greet / help / bye

**Date:** 16 Aug 2026
**Thread intent:** Set up `CS3227-2610-MP1` from the submission guideline and implement only the first product increment.
**Verification:** Agent-drafted. Joseph should confirm after running `./gradlew check` and a manual `help` / `bye` session.

## Prompts (summary)

1. Earlier planning: maximize MP1 grades; product = budget tracker with chat UI; reflections 25%; no to-do clone.
2. Follow-up: feature-by-feature workflow; living reflections the agent consults; feature list as a plan only (not a repo file).
3. This session: set up the repo from the required layout (source, `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`, `logs/`) plus AGENTS.md, Gradle, JUnit, Checkstyle, GitHub Actions, fat-JAR plugin.

## What was produced

- Git already initialised on `master`; remote `git@github.com:josephkwok001/CS3227-2610-MP1` was already added by Joseph.
- Gradle Java 17 app named **Budgie** (`seedu.budgie`), OOP split `Ui` / `Parser` / `Command`.
- Commands: `help`, `bye`; other input → `UnknownCommand`.
- Docs matching v0.1 only. Checkstyle config from AddressBook Level 3. CI workflow runs `./gradlew check`.
- `AGENTS.md` + Cursor rule: read Reflections first; append stubs; no `Features.md`.

## Files touched (high level)

- `build.gradle`, `settings.gradle`, `.gitignore`, `.github/workflows/gradle.yml`
- `config/checkstyle/*`
- `src/main/java/seedu/budgie/**`, `src/test/java/seedu/budgie/**`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `docs/Reflections.md`
- `AGENTS.md`, `README.md`, `logs/`
- Gradle wrapper (generated)

## Checks

- Intended: `./gradlew check` (tests + Checkstyle).
- Not done in the prompt itself: GitHub repo visibility, Canvas username quiz, GitHub Issues/milestones, first git commit/push (unless Joseph asked separately).

## Corrections Joseph should look for

- Product name Budgie was agent-chosen.
- Do not treat unimplemented expense commands as shipped.
- Confirm the GitHub remote exists and the repo is **public**.

## Reflection notes

For this increment I asked Cursor to set up the CS3227 repo from the submission guideline and to implement only greet / help / bye. I deliberately did not ask it to build the whole budget tracker. The prompt was scoped that way because reflections are 25% of the grade: if I one-shot the product, I get no evidence of how prompts evolve, and the User Guide would list features that do not exist yet.

Assumptions the model made. I never named the app. Cursor chose Budgie and the package seedu.budgie, targeted Java 17 (my machine, not AddressBook's Java 25), and treated unknown input as an error plus hint instead of Duke style echo. Those are reasonable, but they were its product decisions, not requirements I wrote.

What it got wrong / what I had to watch. It drafted docs and logs claiming I still needed to run the app, and it was unsure the GitHub repo was public. I had already created the remote. The agent only filled the tree. Checkstyle also failed first (case indentation in Parser) before ./gradlew check went green. When I ran ./gradlew run myself, help and bye matched the User Guide, but Gradle's progress bar printed between Budgie's divider lines, so ./gradlew run is a messy way to demo the CLI.

How I verified. `./gradlew check` (agent) plus my own `./gradlew run`: greeting, help, bye, exit 0. I also confirmed the GitHub repo is public on master.

Prompt evolution. The first planning chats were "how do I maximise marks?" and "journal reflections feature by feature." Only then did I say "set up the repo from this guideline." That ordering helped: the agent already had "no Features.md", "UG must match v0.1", and "one increment".

Prompting vs manual work. Generating Gradle, Checkstyle, CI, and the docs skeleton was faster with the agent. I still did git init and git remote add origin myself. Naming the product and locking "this is not a to do list" are cheap to do by hand and expensive to undo later.

Judgement that stayed with me. I accepted the Ui / Parser / Command split so later features do not dump into one class. I accepted documenting only help and bye even though the real app will have expense.

Next time. Put the app name, Java version, and "do not implement later commands" in the first setup prompt. After the agent reports green tests, run the app myself immediately and try one negative input, not only help / bye. Prefer `java -jar` for User Guide screenshots so Gradle progress does not look like part of the UI.
