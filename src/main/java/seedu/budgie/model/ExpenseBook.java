package seedu.budgie.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.budgie.exception.BudgieException;

/**
 * In-memory store of expenses and incomes. Budgie saves this book after each add or delete.
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

    /**
     * Returns the user-facing message when {@code delete} refers to an index that is not listed.
     *
     * @param oneBasedIndex index the user typed
     * @return error message
     */
    public static String unknownIndexMessage(int oneBasedIndex) {
        return "There is no transaction numbered " + oneBasedIndex + ". Use list to see valid indexes.";
    }

    /**
     * Removes the entry shown as {@code oneBasedIndex} in {@code list}.
     *
     * @param oneBasedIndex index from {@code list}, starting at 1
     * @return the removed entry
     * @throws BudgieException if the index does not match a listed transaction
     */
    public Entry delete(int oneBasedIndex) throws BudgieException {
        if (oneBasedIndex < 1 || oneBasedIndex > entries.size()) {
            throw new BudgieException(unknownIndexMessage(oneBasedIndex));
        }
        Entry removed = entries.remove(oneBasedIndex - 1);
        if (removed instanceof Expense) {
            expenseCount--;
        } else if (removed instanceof Income) {
            incomeCount--;
        } else {
            assert false : "unknown entry type";
        }
        return removed;
    }
}
