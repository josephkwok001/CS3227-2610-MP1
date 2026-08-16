# Budgie User Guide

Budgie is a personal budget tracker with a chat-style interface.

This guide describes **v0.1** only. Features that are not listed here are not in the current product.

## 1. Setup

Requirements:

- **Java 17** or later (`java -version`)

### Option A — run from source

In the project root:

```bash
./gradlew run
```

On Windows:

```bat
gradlew.bat run
```

### Option B — run the JAR

```bash
./gradlew shadowJar
java -jar build/libs/budgie-0.1.jar
```

Copy the JAR into an empty folder if you want to run it the same way a release user would:

```bash
java -jar budgie-0.1.jar
```

## 2. Starting Budgie

When Budgie starts, you should see a banner and a greeting similar to:

```
Hello! I'm Budgie, your personal budget tracker.
Type `help` to see what I can do.
```

Empty lines are ignored.

## 3. Features

### 3.1 View available commands: `help`

Shows the commands supported in this version.

Sample input:

```
help
```

Sample output:

```
Here is what I can do for now:
  help  - show this help message
  bye   - exit Budgie
```

### 3.2 Exit: `bye`

Ends the session.

Sample input:

```
bye
```

Sample output:

```
Bye. Keep those coins in the nest!
```

`bye` is case-insensitive (`Bye` and `BYE` also work).

### 3.3 Unrecognised input

Any other command is rejected with a short hint. This is expected in v0.1 (expense tracking is not implemented yet).

Sample input:

```
expense 12.50 /food lunch
```

Sample output:

```
Sorry, I don't understand `expense 12.50 /food lunch`.
Type `help` to see what I can do.
```

## 4. How to test (for peer testers)

1. Run `./gradlew check` — tests and Checkstyle should pass.
2. Run `./gradlew run`.
3. Type `help` — the output should match section 3.1.
4. Type `expense 1` — you should see the unrecognised-input message.
5. Type `bye` — Budgie should print the goodbye message and exit.

If a step above does not match this guide, treat it as a product bug.
