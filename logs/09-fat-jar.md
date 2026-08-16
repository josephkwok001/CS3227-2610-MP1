# Log 09 — Fat JAR packaging

**Date:** 16 Aug 2026
**Thread intent:** Ship a runnable `budgie.jar` for GitHub Release. Creating the Release itself is Joseph’s step after merge.
**Verification:** Agent-drafted. Joseph should run `./gradlew shadowJar` then `java -jar build/libs/budgie.jar`.

## Prompts (summary)

1. Same request as log 08 (finish remaining P0).

## What was produced

- `shadowJar` archive name `budgie.jar`; application main class `seedu.budgie.Launcher`
- JavaFX artifacts for win / mac / mac-aarch64 / linux
- UG/README/DG instructions; no Release created (needs `master` + `gh release`)

## Files touched (high level)

- `build.gradle`
- `docs/UserGuide.md`, `docs/DeveloperGuide.md`, `README.md`
- `logs/09-fat-jar.md`

## Checks

- `./gradlew shadowJar` — passed (agent), wrote `build/libs/budgie.jar` (~10 MB)
- `java -jar` and GitHub Release left for Joseph

## Corrections Joseph should look for

- Do not mark the Release as done until the JAR is attached on GitHub.
- Testers need Java 17 even with a fat JAR.
