package seedu.budgie.command;

import seedu.budgie.exception.BudgieException;
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
            + "  find     - find transactions by category, description, or amount (e.g. find food)\n"
            + "  delete   - delete a transaction by its list number (e.g. delete 1)\n"
            + "  bye      - exit Budgie";

    @Override
    public String execute(ExpenseBook expenseBook) throws BudgieException {
        return MESSAGE;
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
