package seedu.budgie.model;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory store of expenses for the current session. Data is not saved to disk yet.
 */
public class ExpenseBook {

    private final List<Expense> expenses = new ArrayList<>();

    /**
     * Adds {@code expense} to the book.
     *
     * @param expense expense to record
     */
    public void add(Expense expense) {
        assert expense != null : "expense should not be null";
        expenses.add(expense);
    }

    /**
     * Returns how many expenses are currently stored.
     *
     * @return number of expenses
     */
    public int size() {
        return expenses.size();
    }
}
