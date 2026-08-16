package seedu.budgie.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.budgie.command.Command;
import seedu.budgie.command.ExitCommand;
import seedu.budgie.command.HelpCommand;
import seedu.budgie.command.UnknownCommand;

public class ParserTest {

    private final Parser parser = new Parser();

    @Test
    public void parse_bye_returnsExitCommand() {
        Command command = parser.parse("bye");
        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_byeWithDifferentCase_returnsExitCommand() {
        Command command = parser.parse("ByE");
        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_help_returnsHelpCommand() {
        Command command = parser.parse("help");
        assertInstanceOf(HelpCommand.class, command);
        assertFalse(command.isExit());
    }

    @Test
    public void parse_unknownInput_returnsUnknownCommand() {
        Command command = parser.parse("expense 12.50");
        assertInstanceOf(UnknownCommand.class, command);
        assertFalse(command.isExit());
    }
}
