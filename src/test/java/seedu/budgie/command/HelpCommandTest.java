package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;

public class HelpCommandTest {

    @Test
    public void execute_containsSupportedCommands() throws BudgieException {
        String message = new HelpCommand().execute(new ExpenseBook());
        assertTrue(message.contains("help"));
        assertTrue(message.contains("expense"));
        assertTrue(message.contains("income"));
        assertTrue(message.contains("list"));
        assertTrue(message.contains("delete"));
        assertTrue(message.contains("bye"));
        assertEquals(HelpCommand.MESSAGE, message);
    }
}
