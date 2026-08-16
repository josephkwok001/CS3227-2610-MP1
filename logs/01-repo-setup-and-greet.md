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
