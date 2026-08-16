package seedu.budgie.command;

import seedu.budgie.model.ExpenseBook;

/**
 * Ends the current Budgie session.
 */
public class ExitCommand implements Command {

    public static final String MESSAGE = "Bye. Keep those coins in the nest!";

    @Override
    public String execute(ExpenseBook expenseBook) {
        return MESSAGE;
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
