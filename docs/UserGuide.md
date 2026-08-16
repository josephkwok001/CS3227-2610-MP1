# Budgie User Guide

Budgie is a personal budget tracker with a chat-style interface.

This guide describes **v0.2** only. Features that are not listed here are not in the current product.

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
  help     - show this help message
  expense  - add an expense (e.g. expense 12.50 /food lunch)
  bye      - exit Budgie
```

### 3.2 Add an expense: `expense`

Records an expense in memory for the current session.

Format:

```
expense AMOUNT /CATEGORY DESCRIPTION
```

- `AMOUNT` must be a positive number with at most 2 decimal places (e.g. `12.50`)
- `CATEGORY` starts with `/` and is one word (e.g. `/food`)
- `DESCRIPTION` is the rest of the line (e.g. `lunch`)
- The command word is case-insensitive; the description keeps your capitalisation

Sample input:

```
expense 12.50 /food lunch
```

Sample output:

```
Added expense: $12.50 /food lunch
```

Expenses are **not listed** and **not saved** to disk yet. They exist only until you type `bye`.

Invalid formats print a usage message instead of recording an expense:

```
expense 12.50 /food
```

```
Expense must be: expense AMOUNT /CATEGORY DESCRIPTION
Example: expense 12.50 /food lunch
```

Zero, negative, or extra-decimal amounts (e.g. `0`, `-1`, `12.555`) are also rejected.

### 3.3 Exit: `bye`

Ends the session. Any expenses recorded in this run are discarded.

Sample input:

```
bye
```

Sample output:

```
Bye. Keep those coins in the nest!
```

`bye` is case-insensitive (`Bye` and `BYE` also work).

### 3.4 Unrecognised input

Any other command is rejected with a short hint.

Sample input:

```
income 2500 /salary August pay
```

Sample output:

```
Sorry, I don't understand `income 2500 /salary August pay`.
Type `help` to see what I can do.
```

## 4. How to test (for peer testers)

1. Run `./gradlew check` — tests and Checkstyle should pass.
2. Run `./gradlew run`.
3. Type `help` — the output should match section 3.1.
4. Type `expense 12.50 /food lunch` — you should see `Added expense: $12.50 /food lunch`.
5. Type `expense 12.50 /food` — you should see the usage message from section 3.2.
6. Type `bye` — Budgie should print the goodbye message and exit.

If a step above does not match this guide, treat it as a product bug.
