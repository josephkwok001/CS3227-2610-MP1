package seedu.budgie.command;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Entry;
import seedu.budgie.model.ExpenseBook;

/**
 * Deletes a transaction by the 1-based index shown in {@code list}.
 */
public class DeleteCommand implements Command {

    private final int oneBasedIndex;

    /**
     * Creates a delete command for {@code oneBasedIndex}.
     *
     * @param oneBasedIndex index from {@code list}, starting at 1
     */
    public DeleteCommand(int oneBasedIndex) {
        this.oneBasedIndex = oneBasedIndex;
    }

    @Override
    public String execute(ExpenseBook expenseBook) throws BudgieException {
        Entry removed = expenseBook.delete(oneBasedIndex);
        return "Deleted: " + removed.toListLine();
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public boolean modifiesData() {
        return true;
    }

    public int getOneBasedIndex() {
        return oneBasedIndex;
    }
}
