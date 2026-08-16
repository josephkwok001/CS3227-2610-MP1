package seedu.budgie.parser;

import seedu.budgie.command.Command;
import seedu.budgie.command.ExitCommand;
import seedu.budgie.command.HelpCommand;
import seedu.budgie.command.UnknownCommand;

/**
 * Converts raw user input into a {@link Command}.
 */
public class Parser {

    /**
     * Parses {@code input} into the corresponding command.
     *
     * @param input trimmed user input
     * @return a command that can be executed
     */
    public Command parse(String input) {
        assert input != null : "input should not be null";
        String commandWord = input.trim().toLowerCase();
        switch (commandWord) {
            case "bye":
                return new ExitCommand();
            case "help":
                return new HelpCommand();
            default:
                return new UnknownCommand(input.trim());
        }
    }
}
