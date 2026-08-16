# Budgie User Guide

Budgie is a personal budget tracker with a chat-style interface.

This guide describes **v1.0** only. Features that are not listed here are not in the current product.

## 1. Setup

Requirements:

- **Java 17** or later (`java -version`)

### Option A — run from source

In the project root. A chat window should open:

```bash
./gradlew run
```

On Windows:

```bat
gradlew.bat run
```

The text-only CLI is still available as `./gradlew runCli` if you need it.

### Option B — run the JAR

Build a fat JAR:

```bash
./gradlew shadowJar
java -jar build/libs/budgie.jar
```

Peer testers can download `budgie.jar` from the GitHub Releases page instead of building. Copy it into an empty folder, then:

```bash
java -jar budgie.jar
```

Saved data is written to `data/budgie.txt` in the folder you ran the JAR from.

## 2. Starting Budgie

When Budgie starts, a window titled **Budgie** should open. Type commands in the text field at the bottom and press **Enter** or **Send**.

The first reply is a greeting similar to:

```
Hello! I'm Budgie, your personal budget tracker.
Type `help` to see what I can do.
```

Empty input is ignored. `bye` shows the goodbye message and then closes the window.

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
  income   - add income (e.g. income 2500 /salary August pay)
  list     - show all expenses and incomes
  delete   - delete a transaction by its list number (e.g. delete 1)
  bye      - exit Budgie
```

### 3.2 Add an expense: `expense`

Records an expense. It is saved to `data/budgie.txt` as soon as it is added.

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

Invalid formats print a usage message instead of recording an expense:

```
expense 12.50 /food
```

```
Expense must be: expense AMOUNT /CATEGORY DESCRIPTION
Example: expense 12.50 /food lunch
```

A missing amount (`expense` or `expense /food lunch`) and a negative amount (`expense -1 /food lunch`) have their own messages. See section 3.8.

Zero or extra-decimal amounts (e.g. `0`, `12.555`) are rejected with:

```
Amount must be a positive number with up to 2 decimal places.
```

### 3.3 Add income: `income`

Records income. The format is the same as `expense`. The income is saved immediately.

Format:

```
income AMOUNT /CATEGORY DESCRIPTION
```

Sample input:

```
income 2500 /salary August pay
```

Sample output:

```
Added income: $2500.00 /salary August pay
```

Invalid formats print a usage message:

```
income 2500 /salary
```

```
Income must be: income AMOUNT /CATEGORY DESCRIPTION
Example: income 2500 /salary August pay
```

Zero or extra-decimal amounts are rejected as with `expense`. Missing and negative amounts use the messages in section 3.8.

### 3.4 List transactions: `list`

Shows all expenses and incomes in the order they were added. Numbering starts at 1.

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

If nothing has been added yet:

```
No transactions yet. Add an expense or income first.
```

`list` is case-insensitive. After you `delete` a row, later numbers shift down on the next `list`. The list is loaded from `data/budgie.txt` when Budgie starts.

### 3.5 Delete a transaction: `delete`

Removes one transaction using the number shown by `list`.

Format:

```
delete INDEX
```

Sample session:

```
list
delete 1
list
```

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

Invalid indexes (`delete`, `delete 0`) print the delete usage message. A number that is not on the list (`delete 99` when only one row exists) prints:

```
There is no transaction numbered 99. Use list to see valid indexes.
```

Nothing is removed in that case. Use `list` again if you are unsure of the number.

Deletes are saved immediately. If you delete the last remaining transaction, the save file becomes empty, so a later restart still shows an empty list.

### 3.6 Saved data

Budgie writes `data/budgie.txt` in the folder you ran it from (the project root if you used `./gradlew run`). There is no `save` command.

- A missing file is normal on the first run. Budgie starts with an empty list.
- After `expense`, `income`, or `delete`, the file is updated before the next prompt.
- If some lines in the file are invalid, Budgie skips them and prints a short warning, then loads the rest.

### 3.7 Exit: `bye`

Ends the session. Transactions already saved stay on disk for the next launch.

Sample input:

```
bye
```

Sample output:

```
Bye. Keep those coins in the nest!
```

`bye` is case-insensitive (`Bye` and `BYE` also work).

### 3.8 Errors

Bad input does not crash Budgie. These four cases have distinct messages.

**Unknown command**

```
find food
```

```
Sorry, I don't understand `find food`.
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

## 4. How to test (for peer testers)

If `data/budgie.txt` already exists from an earlier run, `list` may show extra rows. Delete those rows first, or remove the file, before following the numbered steps.

1. Run `./gradlew check` — tests and Checkstyle should pass.
2. Run `./gradlew run` — a Budgie chat window should open.
3. Type `help` in the text field and press Enter — the output should match section 3.1.
4. Type `expense 12.50 /food lunch` — you should see `Added expense: $12.50 /food lunch`.
5. Type `income 2500 /salary August pay` — you should see `Added income: $2500.00 /salary August pay`.
6. Type `income 2500 /salary` — you should see the usage message from section 3.3.
7. Type `list` — you should see numbered `[expense]` and `[income]` lines matching section 3.4.
8. Type `delete 1` — you should see `Deleted: [expense] $12.50 /food lunch`.
9. Type `list` — only the income should remain, now as number 1.
10. Type `bye` — Budgie should print the goodbye message and exit.
11. Run `./gradlew run` again. Type `list` — the income from step 5 should still be there as number 1.
12. Type `find food` — you should see the unknown-command message from section 3.8.
13. Type `expense /food lunch` — you should see the missing-amount message from section 3.8.
14. Type `expense -1 /food lunch` — you should see `Amount cannot be negative.`
15. Type `delete 99` — you should see the unknown-index message from section 3.8. The list must stay unchanged.
16. Type `bye` — you should see the goodbye message, then the window should close.

If a step above does not match this guide, treat it as a product bug.
