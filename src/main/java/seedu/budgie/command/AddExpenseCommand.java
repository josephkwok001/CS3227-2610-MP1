package seedu.budgie.command;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;

/**
 * Adds one expense to the in-memory expense book.
 */
public class AddExpenseCommand implements Command {

    private final Expense expense;

    /**
     * Creates a command that will add {@code expense}.
     *
     * @param expense expense to add
     */
    public AddExpenseCommand(Expense expense) {
        assert expense != null : "expense should not be null";
        this.expense = expense;
    }

    @Override
    public String execute(ExpenseBook expenseBook) throws BudgieException {
        expenseBook.add(expense);
        return "Added expense: " + expense.toDisplayString();
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public boolean modifiesData() {
        return true;
    }

    public Expense getExpense() {
        return expense;
    }
}
