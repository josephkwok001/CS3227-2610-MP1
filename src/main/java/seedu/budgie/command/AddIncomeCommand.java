package seedu.budgie.command;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;

/**
 * Adds one income to the in-memory expense book.
 */
public class AddIncomeCommand implements Command {

    private final Income income;

    /**
     * Creates a command that will add {@code income}.
     *
     * @param income income to add
     */
    public AddIncomeCommand(Income income) {
        assert income != null : "income should not be null";
        this.income = income;
    }

    @Override
    public String execute(ExpenseBook expenseBook) throws BudgieException {
        expenseBook.add(income);
        return "Added income: " + income.toDisplayString();
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public boolean modifiesData() {
        return true;
    }

    public Income getIncome() {
        return income;
    }
}
