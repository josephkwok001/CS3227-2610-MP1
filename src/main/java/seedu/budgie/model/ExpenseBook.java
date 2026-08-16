package seedu.budgie.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory store of expenses and incomes for the current session. Data is not saved to disk yet.
 */
public class ExpenseBook {

    private final List<Entry> entries = new ArrayList<>();
    private int expenseCount;
    private int incomeCount;

    /**
     * Adds {@code expense} to the book, preserving insertion order for {@code list}.
     *
     * @param expense expense to record
     */
    public void add(Expense expense) {
        assert expense != null : "expense should not be null";
        entries.add(expense);
        expenseCount++;
    }

    /**
     * Adds {@code income} to the book, preserving insertion order for {@code list}.
     *
     * @param income income to record
     */
    public void add(Income income) {
        assert income != null : "income should not be null";
        entries.add(income);
        incomeCount++;
    }

    /**
     * Returns how many expenses are currently stored.
     *
     * @return number of expenses
     */
    public int size() {
        return expenseCount;
    }

    /**
     * Returns how many incomes are currently stored.
     *
     * @return number of incomes
     */
    public int incomeCount() {
        return incomeCount;
    }

    /**
     * Returns how many expenses and incomes are stored in total.
     *
     * @return total number of entries
     */
    public int totalCount() {
        return entries.size();
    }

    /**
     * Returns an unmodifiable view of entries in the order they were added.
     *
     * @return entries for listing
     */
    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
