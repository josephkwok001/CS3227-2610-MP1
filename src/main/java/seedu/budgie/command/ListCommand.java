package seedu.budgie.command;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Entry;
import seedu.budgie.model.ExpenseBook;

/**
 * Lists all recorded expenses and incomes in the order they were added.
 */
public class ListCommand implements Command {

    public static final String EMPTY_MESSAGE = "No transactions yet. Add an expense or income first.";

    @Override
    public String execute(ExpenseBook expenseBook) throws BudgieException {
        if (expenseBook.totalCount() == 0) {
            return EMPTY_MESSAGE;
        }
        StringBuilder result = new StringBuilder("Here are your transactions:");
        int index = 1;
        for (Entry entry : expenseBook.getEntries()) {
            result.append('\n').append(index).append(". ").append(entry.toListLine());
            index++;
        }
        return result.toString();
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
