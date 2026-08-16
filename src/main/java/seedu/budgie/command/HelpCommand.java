package seedu.budgie.command;

import seedu.budgie.model.ExpenseBook;

/**
 * Shows the list of commands currently supported by Budgie.
 */
public class HelpCommand implements Command {

    public static final String MESSAGE = "Here is what I can do for now:\n"
            + "  help     - show this help message\n"
            + "  expense  - add an expense (e.g. expense 12.50 /food lunch)\n"
            + "  income   - add income (e.g. income 2500 /salary August pay)\n"
            + "  list     - show all expenses and incomes\n"
            + "  bye      - exit Budgie";

    @Override
    public String execute(ExpenseBook expenseBook) {
        return MESSAGE;
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
