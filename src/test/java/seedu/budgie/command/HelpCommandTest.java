package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class HelpCommandTest {

    @Test
    public void execute_containsSupportedCommands() {
        String message = new HelpCommand().execute();
        assertTrue(message.contains("help"));
        assertTrue(message.contains("bye"));
        assertEquals(HelpCommand.MESSAGE, message);
    }
}
