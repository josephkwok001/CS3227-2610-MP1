package seedu.budgie.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

public class StorageTest {

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
    public void load_missingFile_returnsEmptyBook() {
        LoadResult loaded = storage.load();
        assertEquals(0, loaded.getExpenseBook().totalCount());
        assertEquals(0, loaded.getSkippedLineCount());
        assertNull(loaded.getWarningMessage());
        assertFalse(Files.exists(saveFile));
    }

    @Test
    public void saveThenLoad_roundTripsMixedEntriesIncludingPipesInDescription() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch|extra")).execute(book);
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);

        storage.save(book);
        LoadResult loaded = storage.load();

        assertNull(loaded.getWarningMessage());
        assertEquals(2, loaded.getExpenseBook().totalCount());
        assertEquals("E|12.50|food|lunch|extra", loaded.getExpenseBook().getEntries().get(0).toFileString());
        assertEquals("[income] $2500.00 /salary August pay",
                loaded.getExpenseBook().getEntries().get(1).toListLine());
    }

    @Test
    public void save_afterDelete_persistsRemainingEntriesOnly() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);
        new DeleteCommand(1).execute(book);

        storage.save(book);
        ExpenseBook reloaded = storage.load().getExpenseBook();

        assertEquals(0, reloaded.size());
        assertEquals(1, reloaded.incomeCount());
        assertEquals("[income] $2500.00 /salary August pay", reloaded.getEntries().get(0).toListLine());
    }

    @Test
    public void save_emptyBook_writesEmptyFileSoNextLoadIsEmpty() throws BudgieException, IOException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        storage.save(book);
        new DeleteCommand(1).execute(book);
        storage.save(book);

        assertTrue(Files.exists(saveFile));
        assertTrue(Files.readAllLines(saveFile, StandardCharsets.UTF_8).isEmpty());
        assertEquals(0, storage.load().getExpenseBook().totalCount());
    }

    @Test
    public void load_skipsInvalidLinesAndKeepsValidOnes() throws IOException {
        Files.write(saveFile, List.of(
                "not-a-valid-line",
                "E|12.50|food|lunch",
                "E|0|food|zero",
                "I|2500.00|salary|August pay"
        ), StandardCharsets.UTF_8);

        LoadResult loaded = storage.load();
        assertEquals(2, loaded.getSkippedLineCount());
        assertEquals("Skipped 2 invalid line(s) in the data file.", loaded.getWarningMessage());
        assertEquals(1, loaded.getExpenseBook().size());
        assertEquals(1, loaded.getExpenseBook().incomeCount());
    }
}
