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

## Reflection notes

Packaging was bundled with the GUI increment because a GUI that testers cannot launch from a JAR is not P0. Shadow writes build/libs/budgie.jar with Launcher as the main class, and Gradle pulls JavaFX natives for Windows, Linux, and macOS (including Apple Silicon). Creating the actual GitHub Release is still a click I have to do after this is on master, attaching that JAR, because graders download from Releases.

How I verified. `./gradlew shadowJar` wrote build/libs/budgie.jar (~10 MB). java -jar and GitHub Release left for Joseph.

Engineering judgement. Followed AddressBook style JavaFX classifiers instead of the OpenJFX Gradle plugin.

Next time. Optional P1 only if v1.0 peer tests are clean.
