package seedu.budgie.command;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;

/**
 * Responds to input that is not a recognised command.
 */
public class UnknownCommand implements Command {

    private final String input;

    /**
     * Creates a command for unrecognised {@code input}.
     *
     * @param input the raw user input
     */
    public UnknownCommand(String input) {
        this.input = input;
    }

    @Override
    public String execute(ExpenseBook expenseBook) throws BudgieException {
        return "Sorry, I don't understand `" + input + "`.\n"
                + "Type `help` to see what I can do.";
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
