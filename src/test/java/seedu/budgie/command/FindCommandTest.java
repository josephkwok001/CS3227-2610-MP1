package seedu.budgie.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;

public class FindCommandTest {

    @Test
    public void execute_noMatches_showsEmptyMessage() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        assertEquals(FindCommand.NO_MATCH_MESSAGE, new FindCommand("rent").execute(book));
    }

    @Test
    public void execute_emptyBook_showsEmptyMessage() throws BudgieException {
        assertEquals(FindCommand.NO_MATCH_MESSAGE, new FindCommand("food").execute(new ExpenseBook()));
    }

    @Test
    public void execute_matchesCategoryDescriptionAndAmount_keepsListIndexes() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        new AddExpenseCommand(new Expense(new BigDecimal("12.50"), "food", "lunch")).execute(book);
        new AddIncomeCommand(new Income(new BigDecimal("2500"), "salary", "August pay")).execute(book);
        new AddExpenseCommand(new Expense(new BigDecimal("3"), "bus", "to campus")).execute(book);

        assertEquals("Here are the matching transactions:\n1. [expense] $12.50 /food lunch",
                new FindCommand("food").execute(book));
        assertEquals("Here are the matching transactions:\n2. [income] $2500.00 /salary August pay",
                new FindCommand("august").execute(book));
        assertEquals("Here are the matching transactions:\n1. [expense] $12.50 /food lunch",
                new FindCommand("12.50").execute(book));
        assertEquals("Here are the matching transactions:\n1. [expense] $12.50 /food lunch",
                new FindCommand("12.5").execute(book));
        assertEquals(FindCommand.NO_MATCH_MESSAGE, new FindCommand("1").execute(book));
    }
}
