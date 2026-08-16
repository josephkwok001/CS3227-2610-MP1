package seedu.budgie.command;

/**
 * Shows the list of commands currently supported by Budgie.
 */
public class HelpCommand implements Command {

    public static final String MESSAGE = "Here is what I can do for now:\n"
            + "  help  - show this help message\n"
            + "  bye   - exit Budgie";

    @Override
    public String execute() {
        return MESSAGE;
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
