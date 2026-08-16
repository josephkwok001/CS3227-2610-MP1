package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;

public class AddExpenseCommandTest {

    @Test
    public void execute_addsExpenseToBook() throws BudgieException {
        Expense expense = new Expense(new BigDecimal("12.50"), "food", "lunch");
        ExpenseBook book = new ExpenseBook();
        String message = new AddExpenseCommand(expense).execute(book);
        assertEquals(1, book.size());
        assertEquals("Added expense: $12.50 /food lunch", message);
    }
}
