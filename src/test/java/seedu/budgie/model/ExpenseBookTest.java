package seedu.budgie.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import seedu.budgie.exception.BudgieException;

public class ExpenseBookTest {

    private static final Expense FOOD_EXPENSE = new Expense(new BigDecimal("12.50"), "food", "lunch");
    private static final Income SALARY_INCOME = new Income(new BigDecimal("2500"), "salary", "August pay");
    private static final Expense BUS_EXPENSE = new Expense(new BigDecimal("3"), "bus", "to campus");

    @Test
    public void unknownIndexMessage_formatsExpectedText() {
        assertEquals("There is no transaction numbered 99. Use list to see valid indexes.",
                ExpenseBook.unknownIndexMessage(99));
    }

    @Test
    public void delete_validIndexOnMixedList_removesEntryAndUpdatesCounts() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        book.add(FOOD_EXPENSE);
        book.add(SALARY_INCOME);
        book.add(BUS_EXPENSE);
        assertEquals(3, book.totalCount());
        assertEquals(2, book.size());
        assertEquals(1, book.incomeCount());

        Entry removed = book.delete(2);
        assertInstanceOf(Income.class, removed);
        assertEquals(SALARY_INCOME.getAmount(), ((Income) removed).getAmount());
        assertEquals(2, book.totalCount());
        assertEquals(2, book.size());
        assertEquals(0, book.incomeCount());
        assertEquals(FOOD_EXPENSE, book.getEntries().get(0));
        assertEquals(BUS_EXPENSE, book.getEntries().get(1));
    }

    @Test
    public void delete_firstExpenseOnMixedList_updatesExpenseCount() throws BudgieException {
        ExpenseBook book = new ExpenseBook();
        book.add(FOOD_EXPENSE);
        book.add(SALARY_INCOME);

        Entry removed = book.delete(1);
        assertInstanceOf(Expense.class, removed);
        assertEquals(1, book.totalCount());
        assertEquals(0, book.size());
        assertEquals(1, book.incomeCount());
    }

    @Test
    public void delete_indexTooLarge_throwsWithUnknownIndexMessage() {
        ExpenseBook book = new ExpenseBook();
        book.add(FOOD_EXPENSE);

        BudgieException thrown = assertThrows(BudgieException.class, () -> book.delete(99));
        assertEquals(ExpenseBook.unknownIndexMessage(99), thrown.getMessage());
        assertEquals(1, book.totalCount());
        assertEquals(1, book.size());
    }

    @Test
    public void delete_emptyBook_throwsWithUnknownIndexMessage() {
        ExpenseBook book = new ExpenseBook();

        BudgieException thrown = assertThrows(BudgieException.class, () -> book.delete(1));
        assertEquals(ExpenseBook.unknownIndexMessage(1), thrown.getMessage());
        assertEquals(0, book.totalCount());
    }
}
