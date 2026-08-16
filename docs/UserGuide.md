# Budgie User Guide

Budgie is a personal budget tracker with a chat-style interface.

This guide describes **v0.6** only. Features that are not listed here are not in the current product.

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

Zero, negative, or extra-decimal amounts (e.g. `0`, `-1`, `12.555`) are also rejected.

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

Zero, negative, or extra-decimal amounts are rejected, as with `expense`.

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

Invalid indexes (`delete`, `delete 0`, `delete 99` when the list is shorter) are rejected. Use `list` again if you are unsure of the number.

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

### 3.8 Unrecognised input

Any other command is rejected with a short hint.

Sample input:

```
find food
```

Sample output:

```
Sorry, I don't understand `find food`.
Type `help` to see what I can do.
```

## 4. How to test (for peer testers)

If `data/budgie.txt` already exists from an earlier run, `list` may show extra rows. Delete those rows first, or remove the file, before following the numbered steps.

1. Run `./gradlew check` — tests and Checkstyle should pass.
2. Run `./gradlew run`.
3. Type `help` — the output should match section 3.1.
4. Type `expense 12.50 /food lunch` — you should see `Added expense: $12.50 /food lunch`.
5. Type `income 2500 /salary August pay` — you should see `Added income: $2500.00 /salary August pay`.
6. Type `income 2500 /salary` — you should see the usage message from section 3.3.
7. Type `list` — you should see numbered `[expense]` and `[income]` lines matching section 3.4.
8. Type `delete 1` — you should see `Deleted: [expense] $12.50 /food lunch`.
9. Type `list` — only the income should remain, now as number 1.
10. Type `bye` — Budgie should print the goodbye message and exit.
11. Run `./gradlew run` again. Type `list` — the income from step 5 should still be there as number 1.
12. Type `bye` to exit.

If a step above does not match this guide, treat it as a product bug.
