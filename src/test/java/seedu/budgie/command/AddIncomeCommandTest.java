package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;

public class AddIncomeCommandTest {

    @Test
    public void execute_addsIncomeToBook() throws BudgieException {
        Income income = new Income(new BigDecimal("2500"), "salary", "August pay");
        ExpenseBook book = new ExpenseBook();
        String message = new AddIncomeCommand(income).execute(book);
        assertEquals(1, book.incomeCount());
        assertEquals(0, book.size());
        assertEquals("Added income: $2500.00 /salary August pay", message);
    }
}
