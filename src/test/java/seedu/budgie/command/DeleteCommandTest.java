package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;

public class DeleteCommandTest {

    @Test
    public void execute_validIndex_removesEntryAndRenumbersList() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);

        String message = new DeleteCommand(1).execute(book);
        assertEquals("Deleted: [expense] $12.50 /food lunch", message);
        assertEquals(0, book.size());
        assertEquals(1, book.incomeCount());

        String list = new ListCommand().execute(book);
        assertEquals("Here are your transactions:\n1. [income] $2500.00 /salary August pay", list);
    }

    @Test
    public void execute_indexTooLarge_throwsBudgieException() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        assertThrows(BudgieException.class, () -> new DeleteCommand(2).execute(book));
        assertEquals(1, book.size());
    }
}
