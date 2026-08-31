# Log 19 — Java SE 25 and cross-platform JAR fix

**Date:** 31 Aug 2026  
**Thread intent:** Align with CS3227 requirement (Java SE 25 desktop app, Windows/Linux/macOS) and fix fat JAR native libraries on Apple Silicon.  
**Verification:** `./gradlew check` and `./gradlew release` green; `java --enable-native-access=ALL-UNNAMED -jar release/budgie.jar` starts on Java 25 (Apple Silicon).

---

## Problem found

- Project targeted Java 17; course default is **Java SE 25**.
- Fat JAR merged both `mac` (x86_64) and `mac-aarch64` JavaFX natives at the same path; **x86_64 won**, so `java -jar` failed on Apple Silicon with plain JDK 21/25 (worked only with Zulu FX JDK 17 using bundled arm64 JavaFX).

## Changes

- **`build.gradle`:** Gradle Java **25** toolchain; JavaFX platforms `win`, `linux`, `mac-aarch64` only.
- **`.github/workflows/gradle.yml`:** CI matrix `ubuntu-latest`, `windows-latest`, `macos-latest` with JDK 25.
- **Docs:** README, UserGuide, DeveloperGuide updated for Java 25, `--enable-native-access=ALL-UNNAMED`, OS FAQ (Intel Mac limitation documented).
- **`release/budgie.jar`:** Rebuilt (~10 MB, arm64 macOS dylibs).

## Checks

- `./gradlew check` — 79 tests, Checkstyle green (Java 25 toolchain).
- JAR smoke test on OpenJDK 25 + Apple Silicon — GUI process starts.

## Reflection notes

Cross-platform fat JARs cannot put two macOS architectures at the same native library path. I chose Apple Silicon macOS natives because that matches my dev machine and Java 25 testing; Intel Mac is documented as out of scope for the single submission JAR. Windows and Linux `.dll` / `.so` files coexist with macOS `.dylib` in the same jar without conflict.
