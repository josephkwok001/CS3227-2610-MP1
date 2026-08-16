package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.budgie.model.ExpenseBook;

public class HelpCommandTest {

    @Test
    public void execute_containsSupportedCommands() {
        String message = new HelpCommand().execute(new ExpenseBook());
        assertTrue(message.contains("help"));
        assertTrue(message.contains("expense"));
        assertTrue(message.contains("bye"));
        assertEquals(HelpCommand.MESSAGE, message);
    }
}
