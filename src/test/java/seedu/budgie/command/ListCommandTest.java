package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;

public class ListCommandTest {

    @Test
    public void execute_emptyBook_showsEmptyMessage() throws BudgieException {
        String message = new ListCommand().execute(new ExpenseBook());
        assertEquals(ListCommand.EMPTY_MESSAGE, message);
    }

    @Test
    public void execute_mixedEntries_preservesInsertionOrder() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);
        new AddExpenseCommand(new Expense(new BigDecimal("3"), "bus", "to campus")).execute(book);

        String expected = "Here are your transactions:\n"
                + "1. [expense] $12.50 /food lunch\n"
                + "2. [income] $2500.00 /salary August pay\n"
                + "3. [expense] $3.00 /bus to campus";
        assertEquals(expected, new ListCommand().execute(book));
    }
}
