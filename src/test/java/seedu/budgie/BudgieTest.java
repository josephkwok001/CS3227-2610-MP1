package seedu.budgie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.budgie.command.ListCommand;
import seedu.budgie.storage.Storage;

/**
 * Integration tests for {@link Budgie#getResponse(String)} across parser, commands, model, and storage.
 *
 * <p>Uses {@code data/budgie.txt} under Gradle's test {@code workingDir} (see {@code build.gradle}).
 * Java 17 caches the default directory at JVM start, so {@code user.dir} cannot be repointed per test.
 */
public class BudgieTest {

    private static final Path SAVE_FILE = Path.of(Storage.DEFAULT_FILE_PATH);

    @BeforeEach
    public void setUpEmptySaveFile() throws IOException {
        Files.createDirectories(SAVE_FILE.getParent());
        Files.deleteIfExists(SAVE_FILE);
    }

    @Test
    public void getResponse_addExpenseThenList_showsExactMessages() throws IOException {
        Budgie budgie = new Budgie();

        CommandResult added = budgie.getResponse("expense 12.50 /food lunch");
        assertEquals("Added expense: $12.50 /food lunch", added.getMessage());
        assertFalse(added.isExit());

        CommandResult listed = budgie.getResponse("list");
        assertEquals("Here are your transactions:\n1. [expense] $12.50 /food lunch", listed.getMessage());
        assertEquals(List.of("E|12.50|food|lunch"), readSaveFile());
    }

    @Test
    public void getResponse_deleteAfterAdd_persistsForNewBudgieInstance() throws IOException {
        Budgie budgie = new Budgie();
        budgie.getResponse("expense 12.50 /food lunch");
        budgie.getResponse("income 2500 /salary August pay");
        budgie.getResponse("delete 1");

        Budgie reloaded = new Budgie();
        CommandResult listed = reloaded.getResponse("list");
        assertEquals("Here are your transactions:\n1. [income] $2500.00 /salary August pay",
                listed.getMessage());
        assertEquals(List.of("I|2500.00|salary|August pay"), readSaveFile());
    }

    @Test
    public void getResponse_summaryAfterAdds_includesNetLine() {
        Budgie budgie = new Budgie();
        budgie.getResponse("expense 12.50 /food lunch");
        budgie.getResponse("income 2500 /salary August pay");

        CommandResult summary = budgie.getResponse("summary");
        assertTrue(summary.getMessage().contains("Net: $2487.50"));
    }

    @Test
    public void getResponse_budgetCommand_leavesBookAndFileUnchanged() throws IOException {
        Budgie budgie = new Budgie();

        CommandResult unknown = budgie.getResponse("budget 800");
        assertEquals("Sorry, I don't understand `budget 800`.\n"
                + "Type `help` to see what I can do.", unknown.getMessage());

        CommandResult listed = budgie.getResponse("list");
        assertEquals(ListCommand.EMPTY_MESSAGE, listed.getMessage());
        assertFalse(Files.exists(SAVE_FILE));
    }

    @Test
    public void getResponse_addExpense_writesSaveFile() throws IOException {
        Budgie budgie = new Budgie();
        assertFalse(Files.exists(SAVE_FILE));

        budgie.getResponse("expense 12.50 /food lunch");

        assertTrue(Files.exists(SAVE_FILE));
        assertEquals(List.of("E|12.50|food|lunch"), readSaveFile());
    }

    @Test
    public void getResponse_readOnlyCommands_doNotChangeSaveFile() throws IOException {
        Budgie budgie = new Budgie();
        budgie.getResponse("expense 12.50 /food lunch");
        budgie.getResponse("income 2500 /salary August pay");
        List<String> fileBefore = readSaveFile();

        budgie.getResponse("help");
        budgie.getResponse("find food");
        budgie.getResponse("summary");

        assertEquals(fileBefore, readSaveFile());
    }

    private List<String> readSaveFile() throws IOException {
        return Files.readAllLines(SAVE_FILE, StandardCharsets.UTF_8);
    }
}
