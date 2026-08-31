# Budgie User Guide

Budgie is a desktop **personal budget tracker** with a **chat-style JavaFX window**. You type commands (for example `expense 12.50 /food lunch`) and Budgie replies in the conversation.

This guide describes **v1.2** only. Commands that are not listed here are not in the product.

If a term is unfamiliar, see the [Glossary](#glossary).

## How to use this guide

1. [Getting started](#getting-started) — install Java SE 25, download the JAR, open the window.
2. [Command summary](#command-summary) — one-line formats and examples.
3. [Notes about command format](#notes-about-command-format) — amounts, categories, indexes.
4. [Features](#features) — full formats, sample input, and sample output.
5. [How to test](#how-to-test-for-peer-testers) — numbered path for peer testers.
6. [FAQ](#faq)
7. [Known issues](#known-issues)
8. [Glossary](#glossary)

## Getting started

1. Ensure you have **Java SE 25** (`java -version`). CS3227 uses Java 25 as the default; older JDKs cannot run the v1.2 JAR.

   **Windows:** Start Menu → Command Prompt or PowerShell → `java -version`.

   **macOS:** Terminal → `java -version`.

   Install guides: [Windows](https://se-education.org/guides/tutorials/javaInstallationWindows.html), [macOS](https://se-education.org/guides/tutorials/javaInstallationMac.html). [Adoptium Temurin 25](https://adoptium.net/) works on Windows, Linux, and macOS.

2. Get **v1.2** `budgie.jar` (includes `find` and `summary`):

   - **Recommended for CS3227 submission / peer testing:** use `release/budgie.jar` in this repository (build with `./gradlew release` if needed). It is a fat JAR with JavaFX bundled for **Windows, Linux, and Apple Silicon macOS**.
   - **Optional:** download from [GitHub Releases](https://github.com/josephkwok001/CS3227-2610-MP1/releases) when a v1.2 (or later) tag is published.
   - **Developers:** `./gradlew run` from source also works.

   The published **v1.0** GitHub Release JAR does **not** include `find` or `summary`.

3. Copy the JAR into an empty folder (or run from the repo’s `release/` folder). That folder becomes Budgie’s home folder (`data/budgie.txt` is created there when you add a transaction).

4. Open a terminal, `cd` into that folder, and run:

   ```bash
   java --enable-native-access=ALL-UNNAMED -jar budgie.jar
   ```

   Example from the repository after building:

   ```bash
   java --enable-native-access=ALL-UNNAMED -jar release/budgie.jar
   ```

   On **Java 22+**, the `--enable-native-access=ALL-UNNAMED` flag lets JavaFX load its bundled native libraries. Omitting it may still work on some machines but can fail on Java 25.

   A window titled **Budgie** should open. Type in the text field at the bottom and press **Enter** or **Send**.

   **From source (developers):**

   ```bash
   ./gradlew run
   ```

   On Windows: `gradlew.bat run`. The text-only CLI is `./gradlew runCli` if you need it.

5. Try:

   - `help`
   - `expense 12.50 /food lunch`
   - `list`
   - `summary`
   - `find food`
   - `bye` (shows a goodbye message, then closes the window)

## Command summary

| Action | Format | Example |
|---|---|---|
| View help | `help` | `help` |
| Add an expense | `expense AMOUNT /CATEGORY DESCRIPTION` | `expense 12.50 /food lunch` |
| Add income | `income AMOUNT /CATEGORY DESCRIPTION` | `income 2500 /salary August pay` |
| List transactions | `list` | `list` |
| Find transactions | `find KEYWORD` | `find food` |
| View a summary | `summary` | `summary` |
| Delete a transaction | `delete INDEX` | `delete 1` |
| Exit | `bye` | `bye` |

There is no `save` command. Add, income, and delete write `data/budgie.txt` immediately.

## Notes about command format

- Words in `UPPER_CASE` are values you type. In `expense AMOUNT /CATEGORY DESCRIPTION`, an amount might be `12.50`.
- **Amount** must be a **positive** number with **at most 2 decimal places** (for example `12.50` or `2500`). `0`, `-1`, and `12.555` are rejected. Displayed money always shows two decimal places (for example `$2500.00`).
- **Category** is one word after `/` (for example `/food`). There is no space between `/` and the category name.
- **Description** is the rest of the line. It keeps your capitalisation (`August pay` stays `August pay`).
- The **command word** is case-insensitive (`Help`, `EXPENSE`, `Bye` work). Extra words after `help`, `list`, or `summary` are ignored.
- **Index** for `delete` is the **1-based** number shown by `list`. After a delete, later rows **renumber**. Indexes are not permanent IDs.
- Empty input is ignored. Unknown commands (for example `budget 800`) are rejected; they are not features.

## Features

### Viewing help: `help`

Shows the commands supported in this version.

Format: `help`

In the **GUI**, command names appear in bold monospace; the wording matches the sample below (CLI shows the same text).

Sample input:

```
help
```

Sample output:

```
Here is what I can do for now:

help — show this help message
expense — add an expense (e.g. expense 12.50 /food lunch)
income — add income (e.g. income 2500 /salary August pay)
list — show all expenses and incomes
find — find transactions by category, description, or amount (e.g. find food)
summary — show totals and a breakdown by category
delete — delete a transaction by its list number (e.g. delete 1)
bye — exit Budgie
```

### Adding an expense: `expense`

Records an expense and saves it immediately.

Format: `expense AMOUNT /CATEGORY DESCRIPTION`

Sample input:

```
expense 12.50 /food lunch
```

Sample output:

```
Added expense: $12.50 /food lunch
```

If the line is the wrong shape (for example missing a description):

```
expense 12.50 /food
```

```
Expense must be: expense AMOUNT /CATEGORY DESCRIPTION
Example: expense 12.50 /food lunch
```

A missing amount (`expense` or `expense /food lunch`) and a negative amount (`expense -1 /food lunch`) have their own messages under [Errors](#errors).

Zero or extra-decimal amounts (for example `0`, `12.555`) are rejected with:

```
Amount must be a positive number with up to 2 decimal places.
```

### Adding income: `income`

Records income. The argument shape is the same as `expense`. The income is saved immediately.

Format: `income AMOUNT /CATEGORY DESCRIPTION`

Sample input:

```
income 2500 /salary August pay
```

Sample output:

```
Added income: $2500.00 /salary August pay
```

If the description is missing:

```
income 2500 /salary
```

```
Income must be: income AMOUNT /CATEGORY DESCRIPTION
Example: income 2500 /salary August pay
```

Missing and negative amounts use the same rules as `expense`.

### Listing transactions: `list`

Shows expenses and incomes in the order they were added. Numbering starts at 1.

Sample session:

```
expense 12.50 /food lunch
income 2500 /salary August pay
list
```

Sample `list` output:

```
Here are your transactions:
1. [expense] $12.50 /food lunch
2. [income] $2500.00 /salary August pay
```

If nothing has been added yet (and nothing is in the save file):

```
No transactions yet. Add an expense or income first.
```

The list is loaded from `data/budgie.txt` when Budgie starts.

### Finding transactions: `find`

Shows transactions whose **category**, **description**, or **amount** matches `KEYWORD`. Matching is case-insensitive. `find August pay` uses the whole remainder of the line as one keyword.

Format: `find KEYWORD`

The numbers in the result are the same as `list`, so you can `delete` that row afterwards.

Sample session (after the two adds in the `list` example):

```
find food
```

```
Here are the matching transactions:
1. [expense] $12.50 /food lunch
```

```
find 12.50
```

also shows that expense. `find 12.5` matches the same amount. `find 1` does **not** match `$12.50` (it does not treat a digit inside a larger amount as a hit).

If nothing matches:

```
find rent
```

```
No matching transactions found.
```

`find` with no keyword prints usage. `find` does not change saved data.

### Viewing a summary: `summary`

Shows total income, total expenses, **net** (income minus expenses), and those totals grouped by category. There is no monthly budget in this version, so `summary` does not show remaining budget.

Format: `summary`

Sample session (after the two adds in the `list` example):

```
summary
```

```
Here is your summary:
Income: $2500.00
Expenses: $12.50
Net: $2487.50

Expenses by category:
  /food: $12.50

Income by category:
  /salary: $2500.00
```

If nothing has been added yet (and nothing is in the save file), `summary` uses the same empty message as `list`. Several expenses in the same category are added together. If there are only expenses (or only incomes), the other category section is omitted. Net can be negative (`-$15.50`) when expenses are larger than income.

`summary` does not change saved data.

### Deleting a transaction: `delete`

Removes one transaction using the number shown by `list`. The delete is saved immediately.

Format: `delete INDEX`

If `list` first showed:

```
1. [expense] $12.50 /food lunch
2. [income] $2500.00 /salary August pay
```

then `delete 1` prints:

```
Deleted: [expense] $12.50 /food lunch
```

and the remaining income becomes number 1.

`delete` or `delete 0` prints the delete usage message. A number that is not on the list (`delete 99` when only one row exists) prints:

```
There is no transaction numbered 99. Use list to see valid indexes.
```

Nothing is removed in that case. If you delete the last remaining transaction, the save file becomes empty, so a later restart still shows an empty list.

### Saved data

Budgie writes `data/budgie.txt` in the folder you ran it from (the project root if you used `./gradlew run`, or the folder that contains the JAR if you used `java -jar`).

- A missing file on the first run is normal. Budgie starts with an empty list.
- After `expense`, `income`, or `delete`, the file is updated before the next prompt.
- If some lines in the file are invalid, Budgie skips them, shows a short warning, and loads the rest.

Editing the file by hand is possible (one line per transaction: `E|12.50|food|lunch` or `I|2500.00|salary|August pay`). A description may contain `|` (for example `lunch|extra` is stored as `E|12.50|food|lunch|extra`). If a line is invalid, that line is skipped on the next launch.

### Exiting: `bye`

Ends the session. Data already saved stays on disk.

Sample input:

```
bye
```

Sample output:

```
Bye. Keep those coins in the nest!
```

In the GUI, the window then closes after a short pause. `Bye` and `BYE` also work.

### Errors

Bad input does not crash Budgie. These four cases have distinct messages.

**Unknown command**

```
budget 800
```

```
Sorry, I don't understand `budget 800`.
Type `help` to see what I can do.
```

**Missing amount**

```
expense /food lunch
```

```
Expense is missing an amount.
Example: expense 12.50 /food lunch
```

The same idea applies to `income` with no amount.

**Negative amount**

```
expense -1 /food lunch
```

```
Amount cannot be negative.
```

**Unknown delete index**

If `list` has fewer than 99 rows:

```
delete 99
```

```
There is no transaction numbered 99. Use list to see valid indexes.
```

## How to test (for peer testers)

If `data/budgie.txt` already exists from an earlier run, `list` may show extra rows. Delete those rows first, or remove the file, before following the numbered steps.

**Path A — from the JAR (recommended for grading):**

1. Ensure **Java SE 25** is installed (`java -version`).
2. Build or locate `release/budgie.jar` (run `./gradlew release` from the repo if needed).
3. Copy `budgie.jar` into an empty folder, `cd` there, and run `java --enable-native-access=ALL-UNNAMED -jar budgie.jar`.
4. Follow steps 3–19 below (type commands in the Budgie window).

**Path B — from source (developers):**

1. Run `./gradlew check` — tests and Checkstyle should pass.
2. Run `./gradlew run` — a Budgie chat window should open.
3. Follow steps 3–19 below.

**Steps 3–19 (both paths):**

3. Type `help` in the text field and press Enter — the output should match the sample under [Viewing help](#viewing-help-help).
4. Type `expense 12.50 /food lunch` — you should see `Added expense: $12.50 /food lunch`.
5. Type `income 2500 /salary August pay` — you should see `Added income: $2500.00 /salary August pay`.
6. Type `income 2500 /salary` — you should see the usage message under [Adding income](#adding-income-income).
7. Type `list` — you should see numbered `[expense]` and `[income]` lines matching [Listing transactions](#listing-transactions-list).
8. Type `summary` — you should see income, expenses, net, and category totals matching [Viewing a summary](#viewing-a-summary-summary).
9. Type `find food` — you should see the expense as number 1 under [Finding transactions](#finding-transactions-find).
10. Type `find rent` — you should see `No matching transactions found.`
11. Type `delete 1` — you should see `Deleted: [expense] $12.50 /food lunch`.
12. Type `list` — only the income should remain, now as number 1.
13. Type `bye` — you should see the goodbye message, then the window should close.
14. Start Budgie again (`java --enable-native-access=ALL-UNNAMED -jar budgie.jar` or `./gradlew run`). Type `list` — the income from step 5 should still be there as number 1.
15. Type `budget 800` — you should see the unknown-command message under [Errors](#errors).
16. Type `expense /food lunch` — you should see the missing-amount message under [Errors](#errors).
17. Type `expense -1 /food lunch` — you should see `Amount cannot be negative.`
18. Type `delete 99` — you should see the unknown-index message under [Errors](#errors). The list must stay unchanged.
19. Type `bye` — you should see the goodbye message, then the window should close.

If a step above does not match this guide, treat it as a product bug.

Path A uses `release/budgie.jar` (v1.2). The published **v1.0** GitHub Release JAR does not include `find` or `summary`.

## FAQ

**Q: How do I transfer my data to another computer?**
**A:** Copy `data/budgie.txt` from the old home folder into a `data/` folder next to `budgie.jar` on the new computer. You still need **Java SE 25** there.

**Q: Do I need to save before I quit?**
**A:** No. Successful `expense`, `income`, and `delete` commands already wrote the file. `bye` only closes the session.

**Q: Does each transaction keep a fixed number?**
**A:** No. `INDEX` is the current `list` position, starting at 1. After `delete`, later rows move up.

**Q: Why was `budget 800` rejected?**
**A:** There is no `budget` command in this version. Only the commands in the [command summary](#command-summary) are supported.

**Q: Does `summary` show how much budget I have left?**
**A:** No. There is no monthly budget yet. `summary` shows total income, total expenses, **net** (income minus expenses), and totals by category.

**Q: Can I `delete` using a number from `find`?**
**A:** Yes. `find` prints the same numbers as `list`. `delete 1` still means the first row of the full list.

**Q: Which operating systems does Budgie run on?**
**A:** **Windows, Linux, and macOS (Apple Silicon).** The fat JAR bundles JavaFX natives for those platforms. Use **Java SE 25** and run with `java --enable-native-access=ALL-UNNAMED -jar budgie.jar`. Intel-based Macs are not covered by the bundled macOS natives; use Windows/Linux, ask a classmate to test on your OS via the forum, or run from source with `./gradlew run`.

**Q: Do I need programming knowledge?**
**A:** No. You only need to type the commands in this guide.

**Q: What if I edit `data/budgie.txt` and make a mess?**
**A:** Invalid lines are skipped on the next launch, and Budgie warns you. Valid lines still load. Back up the file before editing it.

**Q: Amounts look like `$2500.00` even when I typed `2500`. Is that wrong?**
**A:** No. Budgie always displays two decimal places. You may type `2500` or `2500.00`.

## Known issues

1. **`./gradlew run` mixes Gradle progress text with the old CLI.** Prefer `java --enable-native-access=ALL-UNNAMED -jar release/budgie.jar` (or `./gradlew run` for the GUI) when you demo the product. `./gradlew runCli` is the text-only loop and can look messy under Gradle.
2. **The save file follows the working directory.** If you launch the JAR from two different folders, you get two different `data/budgie.txt` files.
3. **There are no automated GUI tests.** Behaviour of the window is checked by following [How to test](#how-to-test-for-peer-testers).
4. **`./gradlew check` uses an isolated test folder (`build/test-run`).** Automated tests do not read or write your project’s `data/budgie.txt`.
5. **Java 22+ may require `--enable-native-access=ALL-UNNAMED`** when running `java -jar` so JavaFX can load bundled native libraries.
6. **One fat JAR cannot ship both Intel and Apple Silicon macOS natives** at the same path. The submission JAR targets Apple Silicon macOS; Intel Mac users should use another OS or run from source.

## Glossary

| Term | Meaning |
|---|---|
| **Amount** | Money value for an expense or income. Must be positive, at most two decimal places. |
| **Category** | One-word label after `/`, for example `/food` or `/salary`. |
| **CLI** | Text-only interface (`./gradlew runCli`). |
| **Command word** | First token of a line (`help`, `expense`, `list`, …). Case-insensitive. |
| **Description** | Free text after category, for example `lunch` or `August pay`. |
| **Entry / transaction** | One recorded expense or income shown by `list`. |
| **Find keyword** | Text after `find`. Matches category, description, or amount. |
| **GUI** | The JavaFX chat window (`./gradlew run` or `java -jar budgie.jar`). |
| **Net** | Income total minus expense total, as shown by `summary`. Can be negative. |
| **Summary** | Totals of income and expenses, including a breakdown by category. |
| **Index** | 1-based row number from the current `list`. Used by `delete`. |
| **Data file** | `data/budgie.txt` next to the working directory; auto-saved after changes. |
| **JAR** | Runnable `budgie.jar` with JavaFX bundled. Use `release/budgie.jar` in this repo (v1.2), or a GitHub Release when published. Requires **Java SE 25** and `--enable-native-access=ALL-UNNAMED` on Java 22+. |
