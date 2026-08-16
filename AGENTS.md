# Agent instructions for CS3227 MP1 (Budgie)

You are helping Joseph build **Budgie**, a personal budget tracker chatbot (Java 17 + Gradle + JUnit + Checkstyle). This is **not** a to-do manager and must not clone CS2103 iP/tP task-list behaviour.

Read this file, then [docs/Reflections.md](docs/Reflections.md) and the latest files in [logs/](logs/) **before** writing code.

## Hard rules

- Implement **one increment at a time**. Do not sneak in later features (GUI, persistence) unless the user asked for that increment.
- **Do not create `Features.md`** or any second feature list in the repo. User-facing features live only in [docs/UserGuide.md](docs/UserGuide.md). The backlog lives outside the repo (Cursor plan).
- Follow the [SE-EDU intermediate Java standard](https://se-education.org/guides/conventions/java/intermediate.html). Keep Checkstyle clean (`./gradlew check`).
- Git commit subjects follow [SE-EDU git convention](https://se-education.org/guides/conventions/git.html) (`Add …`, `Fix …`, `Refactor …`). One logical change per commit.
- Honour decisions already recorded in Reflections (command syntax, design rejections). Do not silently reverse them.
- Do not overwrite Joseph's first-person writing in `docs/Reflections.md`. Only **append** a factual increment stub at the end of the increment journal.

## After every increment (mandatory)

1. Run `./gradlew check` and fix failures.
2. Update [docs/UserGuide.md](docs/UserGuide.md) so it matches the product **exactly**. Do not document unimplemented commands.
3. Write a **factual** session summary to `logs/NN-short-name.md` (prompts, files touched, tests run, what the model got wrong). Joseph must verify it before it is treated as final.
4. **Append** an increment stub to [docs/Reflections.md](docs/Reflections.md) using the template in that file. Write it as notes for Joseph, not as if you are the student.
5. Remind Joseph to rewrite that stub in first person the same day.

## Code structure

Package root: `seedu.budgie`. Preferred split:

- `ui` — user interaction
- `parser` — command parsing
- `command` — command objects
- later: `model`, `storage`, exceptions

Prefer small methods, custom exceptions for user/environment errors, assertions for internal assumptions. Add JUnit for parser, money rules, storage, and summaries — not GUI.

## Current increment order (do not skip ahead)

1. Greet / help / bye
2. Add expense
3. Add income
4. List
5. Delete (current)
6. Persist
7. Errors (remaining edge cases)
8. JavaFX GUI
9. Fat JAR release
10. P1 only if P0 is clean: budget, summary, find, dates, edit
