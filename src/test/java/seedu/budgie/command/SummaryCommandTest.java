package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;

public class SummaryCommandTest {

    @Test
    public void execute_emptyBook_showsEmptyMessage() throws BudgieException {
        assertEquals(ListCommand.EMPTY_MESSAGE, new SummaryCommand().execute(new ExpenseBook()));
    }

    @Test
    public void execute_mixedEntries_totalsAndCategories() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);
        new AddExpenseCommand(new Expense(new BigDecimal("3"), "bus", "to campus")).execute(book);

        String expected = "Here is your summary:\n"
                + "Income: $2500.00\n"
                + "Expenses: $15.50\n"
                + "Net: $2484.50\n"
                + "\n"
                + "Expenses by category:\n"
                + "  /food: $12.50\n"
                + "  /bus: $3.00\n"
                + "\n"
                + "Income by category:\n"
                + "  /salary: $2500.00";
        assertEquals(expected, new SummaryCommand().execute(book));
        assertFalse(new SummaryCommand().modifiesData());
    }

    @Test
    public void execute_sameCategory_mergesAmounts() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        new AddExpenseCommand(new Expense(new BigDecimal("3.00"), "food", "dinner")).execute(book);

        String expected = "Here is your summary:\n"
                + "Income: $0.00\n"
                + "Expenses: $15.50\n"
                + "Net: -$15.50\n"
                + "\n"
                + "Expenses by category:\n"
                + "  /food: $15.50";
        assertEquals(expected, new SummaryCommand().execute(book));
    }

    @Test
    public void execute_incomeOnly_omitsExpenseCategorySection() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);

        String expected = "Here is your summary:\n"
                + "Income: $2500.00\n"
                + "Expenses: $0.00\n"
                + "Net: $2500.00\n"
                + "\n"
                + "Income by category:\n"
                + "  /salary: $2500.00";
        assertEquals(expected, new SummaryCommand().execute(book));
    }
}
