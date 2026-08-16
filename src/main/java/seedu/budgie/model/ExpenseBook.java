package seedu.budgie.model;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory store of expenses and incomes for the current session. Data is not saved to disk yet.
 */
public class ExpenseBook {

    private final List<Expense> expenses = new ArrayList<>();
    private final List<Income> incomes = new ArrayList<>();

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
     * Adds {@code income} to the book.
     *
     * @param income income to record
     */
    public void add(Income income) {
        assert income != null : "income should not be null";
        incomes.add(income);
    }

    /**
     * Returns how many expenses are currently stored.
     *
     * @return number of expenses
     */
    public int size() {
        return expenses.size();
    }

    /**
     * Returns how many incomes are currently stored.
     *
     * @return number of incomes
     */
    public int incomeCount() {
        return incomes.size();
    }
}
