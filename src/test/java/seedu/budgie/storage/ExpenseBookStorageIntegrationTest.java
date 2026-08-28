package seedu.budgie.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.budgie.command.AddExpenseCommand;
import seedu.budgie.command.AddIncomeCommand;
import seedu.budgie.command.DeleteCommand;
import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;
import seedu.budgie.storage.Storage.LoadResult;

/**
 * Integration tests across command, model, and storage layers using real temp files.
 */
public class ExpenseBookStorageIntegrationTest {

    private static final String EXPENSE_LIST_LINE = "[expense] $12.50 /food lunch|extra";
    private static final String INCOME_LIST_LINE = "[income] $2500.00 /salary August pay";
    private static final String EXPENSE_FILE_LINE = "E|12.50|food|lunch|extra";
    private static final String INCOME_FILE_LINE = "I|2500.00|salary|August pay";

    @TempDir
    private Path tempDir;

    private Path saveFile;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        saveFile = tempDir.resolve("budgie.txt");
        storage = new Storage(saveFile);
    }

    @Test
    public void saveLoad_afterAddExpenseAndIncome_preservesListLinesAndFileContents()
            throws BudgieException, IOException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch|extra")).execute(book);
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);

        storage.save(book);
        LoadResult loaded = storage.load();

        assertNull(loaded.getWarningMessage());
        assertEquals(List.of(EXPENSE_FILE_LINE, INCOME_FILE_LINE),
                Files.readAllLines(saveFile, StandardCharsets.UTF_8));
        assertEquals(2, loaded.getExpenseBook().totalCount());
        assertEquals(EXPENSE_LIST_LINE, loaded.getExpenseBook().getEntries().get(0).toListLine());
        assertEquals(INCOME_LIST_LINE, loaded.getExpenseBook().getEntries().get(1).toListLine());
    }

    @Test
    public void saveLoad_afterDeleteFirstEntry_fileAndBookMatchRemaining() throws BudgieException, IOException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);
        new DeleteCommand(1).execute(book);

        storage.save(book);
        LoadResult loaded = storage.load();

        assertEquals(List.of(INCOME_FILE_LINE), Files.readAllLines(saveFile, StandardCharsets.UTF_8));
        assertEquals(1, loaded.getExpenseBook().totalCount());
        assertEquals(0, loaded.getExpenseBook().size());
        assertEquals(1, loaded.getExpenseBook().incomeCount());
        assertEquals(INCOME_LIST_LINE, loaded.getExpenseBook().getEntries().get(0).toListLine());
    }

    @Test
    public void saveLoad_emptyBookAfterDeletingLastEntry_writesEmptyFileAndLoadsEmpty()
            throws BudgieException, IOException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        storage.save(book);
        new DeleteCommand(1).execute(book);
        storage.save(book);

        assertTrue(Files.exists(saveFile));
        assertTrue(Files.readAllLines(saveFile, StandardCharsets.UTF_8).isEmpty());

        LoadResult loaded = storage.load();
        assertNull(loaded.getWarningMessage());
        assertEquals(0, loaded.getExpenseBook().totalCount());
    }
}
