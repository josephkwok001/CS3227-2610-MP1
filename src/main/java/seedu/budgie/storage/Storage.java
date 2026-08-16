package seedu.budgie.storage;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Entry;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;

/**
 * Loads and saves the expense book to a text file.
 */
public class Storage {

    public static final String DEFAULT_FILE_PATH = "data/budgie.txt";

    private static final int FILE_FIELD_COUNT = 4;

    private final Path filePath;

    /**
     * Creates storage that reads and writes {@code data/budgie.txt} in the working directory.
     */
    public Storage() {
        this(Path.of(DEFAULT_FILE_PATH));
    }

    /**
     * Creates storage for {@code filePath}.
     *
     * @param filePath save file location
     */
    public Storage(Path filePath) {
        assert filePath != null : "file path should not be null";
        this.filePath = filePath;
    }

    /**
     * Result of loading a save file. Missing files start as an empty book with no warning.
     */
    public static class LoadResult {

        private final ExpenseBook expenseBook;
        private final int skippedLineCount;
        private final String warningMessage;

        /**
         * Creates a load result.
         *
         * @param expenseBook loaded transactions
         * @param skippedLineCount how many non-empty lines could not be parsed
         * @param warningMessage message to show the user, or {@code null} if load was clean
         */
        public LoadResult(ExpenseBook expenseBook, int skippedLineCount, String warningMessage) {
            this.expenseBook = expenseBook;
            this.skippedLineCount = skippedLineCount;
            this.warningMessage = warningMessage;
        }

        public ExpenseBook getExpenseBook() {
            return expenseBook;
        }

        public int getSkippedLineCount() {
            return skippedLineCount;
        }

        public String getWarningMessage() {
            return warningMessage;
        }
    }

    /**
     * Loads transactions from disk. A missing file is treated as an empty book.
     *
     * @return loaded book plus any user-facing warning
     */
    public LoadResult load() {
        if (!Files.exists(filePath)) {
            return new LoadResult(new ExpenseBook(), 0, null);
        }
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            ExpenseBook expenseBook = new ExpenseBook();
            int skippedLineCount = 0;
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Entry entry = decode(line);
                if (entry == null) {
                    skippedLineCount++;
                    continue;
                }
                addDecodedEntry(expenseBook, entry);
            }
            String warning = skippedLineCount == 0
                    ? null
                    : "Skipped " + skippedLineCount + " invalid line(s) in the data file.";
            return new LoadResult(expenseBook, skippedLineCount, warning);
        } catch (IOException e) {
            return new LoadResult(new ExpenseBook(), 0,
                    "Could not load saved data. Starting with an empty list.");
        }
    }

    /**
     * Writes the current book to disk, including an empty file when every transaction was deleted.
     *
     * @param expenseBook current transactions
     * @throws BudgieException if the file cannot be created or written
     */
    public void save(ExpenseBook expenseBook) throws BudgieException {
        assert expenseBook != null : "expense book should not be null";
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (Entry entry : expenseBook.getEntries()) {
                lines.add(entry.toFileString());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BudgieException("Could not save data to " + filePath + ".");
        }
    }

    private void addDecodedEntry(ExpenseBook expenseBook, Entry entry) {
        if (entry instanceof Expense) {
            expenseBook.add((Expense) entry);
        } else if (entry instanceof Income) {
            expenseBook.add((Income) entry);
        } else {
            assert false : "unknown entry type";
        }
    }

    private Entry decode(String line) {
        String[] parts = line.split("\\|", FILE_FIELD_COUNT);
        if (parts.length != FILE_FIELD_COUNT) {
            return null;
        }
        String type = parts[0].trim();
        BigDecimal amount = parseAmount(parts[1].trim());
        String category = parts[2].trim();
        String description = parts[3].trim();
        if (amount == null || category.isEmpty() || description.isEmpty()) {
            return null;
        }
        if ("E".equals(type)) {
            return new Expense(amount, category, description);
        }
        if ("I".equals(type)) {
            return new Income(amount, category, description);
        }
        return null;
    }

    private BigDecimal parseAmount(String amountText) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText);
        } catch (NumberFormatException e) {
            return null;
        }
        if (amount.scale() > 2 || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return amount;
    }
}
