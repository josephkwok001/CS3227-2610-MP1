# Log 16 — release/ folder with fat JAR

**Date:** 29 Aug 2026  
**Thread intent:** CS3227 submission requires a `release/` folder with the latest Gradle fat JAR (JavaFX included). Previously only `build/libs/budgie.jar` existed via `shadowJar`.  
**Verification:** `./gradlew release` + `./gradlew check`. Joseph should run `java -jar release/budgie.jar` and commit `release/budgie.jar` for submission.

---

## What was missing

- No `release/` directory in the repo.
- `shadowJar` wrote to `build/libs/budgie.jar` only (`build/` is gitignored).
- Docs pointed testers to `build/libs/`, not a submission `release/` path.

---

## What was added

- **`build.gradle`:** `copyReleaseJar` (Copy) and `release` task — depends on `shadowJar`, copies to `release/budgie.jar`.
- **`release/budgie.jar`:** ~10 MB fat JAR with JavaFX natives (win, mac, mac-aarch64, linux).
- **Docs:** `README.md`, `docs/UserGuide.md`, `docs/DeveloperGuide.md` — `./gradlew release` and `release/budgie.jar`.

No production Java changes.

---

## Commands

```bash
./gradlew release          # build + copy to release/budgie.jar
java -jar release/budgie.jar
```

---

## Joseph should verify

- GUI opens from `release/budgie.jar`.
- `find` and `summary` work (v1.2 JAR, not old v1.0 GitHub Release).
- Commit `release/budgie.jar` if the course requires it in the repo (not gitignored).

## Reflection notes

The course asks for a release/ folder with the latest fat JAR. I only had ./gradlew shadowJar writing to gitignored build/libs/. I asked Cursor to add a release Gradle task that copies budgie.jar into release/ after shadowJar, and to update the README/UG/DG. `./gradlew release` produced a ~10 MB JAR with JavaFX.

How I verified. `./gradlew release` and `./gradlew check`. Committed release/budgie.jar for submission.

Next time. Re-run `./gradlew release` before any submission or GitHub Release tag.
