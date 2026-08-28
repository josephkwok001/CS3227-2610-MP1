package seedu.budgie.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import seedu.budgie.command.AddExpenseCommand;
import seedu.budgie.command.AddIncomeCommand;
import seedu.budgie.command.Command;
import seedu.budgie.command.DeleteCommand;
import seedu.budgie.command.ExitCommand;
import seedu.budgie.command.FindCommand;
import seedu.budgie.command.HelpCommand;
import seedu.budgie.command.ListCommand;
import seedu.budgie.command.SummaryCommand;
import seedu.budgie.command.UnknownCommand;
import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;

public class ParserTest {

    private final Parser parser = new Parser();

    @Test
    public void parse_bye_returnsExitCommand() throws BudgieException {
        Command command = parser.parse("bye");
        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_byeWithDifferentCase_returnsExitCommand() throws BudgieException {
        Command command = parser.parse("ByE");
        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_help_returnsHelpCommand() throws BudgieException {
        Command command = parser.parse("help");
        assertInstanceOf(HelpCommand.class, command);
        assertFalse(command.isExit());
    }

    @Test
    public void parse_unknownInput_returnsUnknownCommand() throws BudgieException {
        Command command = parser.parse("budget 800");
        assertInstanceOf(UnknownCommand.class, command);
        assertFalse(command.isExit());
        assertEquals("Sorry, I don't understand `budget 800`.\n"
                + "Type `help` to see what I can do.", command.execute(new ExpenseBook()));
    }

    @Test
    public void parse_validExpense_returnsAddExpenseCommand() throws BudgieException {
        Command command = parser.parse("expense 12.50 /food lunch");
        assertInstanceOf(AddExpenseCommand.class, command);
        AddExpenseCommand addCommand = (AddExpenseCommand) command;
        assertEquals(new BigDecimal("12.50"), addCommand.getExpense().getAmount());
        assertEquals("food", addCommand.getExpense().getCategory());
        assertEquals("lunch", addCommand.getExpense().getDescription());
        assertFalse(command.isExit());
    }

    @Test
    public void parse_expenseDifferentCase_returnsAddExpenseCommand() throws BudgieException {
        Command command = parser.parse("Expense 1 /transport Taxi to campus");
        assertInstanceOf(AddExpenseCommand.class, command);
        AddExpenseCommand addCommand = (AddExpenseCommand) command;
        assertEquals("Taxi to campus", addCommand.getExpense().getDescription());
    }

    @Test
    public void parse_expenseMissingAmount_throwsExpenseMissingAmount() {
        BudgieException empty = assertThrows(BudgieException.class, () -> parser.parse("expense"));
        assertEquals(Parser.EXPENSE_MISSING_AMOUNT, empty.getMessage());
        BudgieException skippedAmount = assertThrows(BudgieException.class, () -> parser.parse("expense /food lunch"));
        assertEquals(Parser.EXPENSE_MISSING_AMOUNT, skippedAmount.getMessage());
    }

    @Test
    public void parse_expenseMissingDescription_throwsBudgieException() {
        BudgieException thrown = assertThrows(BudgieException.class, () -> parser.parse("expense 12.50 /food"));
        assertEquals(Parser.EXPENSE_USAGE, thrown.getMessage());
    }

    @Test
    public void parse_expenseMissingSlashCategory_throwsBudgieException() {
        assertThrows(BudgieException.class, () -> parser.parse("expense 12.50 food lunch"));
    }

    @Test
    public void parse_expense_invalidAmounts() {
        BudgieException zero = assertThrows(BudgieException.class, () -> parser.parse("expense 0 /food lunch"));
        assertEquals(Parser.AMOUNT_INVALID, zero.getMessage());

        String tooManyDecimalsInput = "expense 12.555 /food lunch";
        BudgieException tooManyDecimals = assertThrows(
                BudgieException.class, () -> parser.parse(tooManyDecimalsInput));
        assertEquals(Parser.AMOUNT_INVALID, tooManyDecimals.getMessage());

        BudgieException negative = assertThrows(BudgieException.class, () -> parser.parse("expense -1 /food lunch"));
        assertEquals(Parser.AMOUNT_NEGATIVE, negative.getMessage());
        assertEquals("Amount cannot be negative.", Parser.AMOUNT_NEGATIVE);
    }

    @Test
    public void parse_income_invalidAmounts() {
        BudgieException zero = assertThrows(BudgieException.class, () -> parser.parse("income 0 /salary August pay"));
        assertEquals(Parser.AMOUNT_INVALID, zero.getMessage());

        String tooManyDecimalsInput = "income 12.555 /salary August pay";
        BudgieException tooManyDecimals = assertThrows(
                BudgieException.class, () -> parser.parse(tooManyDecimalsInput));
        assertEquals(Parser.AMOUNT_INVALID, tooManyDecimals.getMessage());

        String negativeInput = "income -5 /salary August pay";
        BudgieException negative = assertThrows(BudgieException.class, () -> parser.parse(negativeInput));
        assertEquals(Parser.AMOUNT_NEGATIVE, negative.getMessage());
    }

    @Test
    public void parse_validIncome_returnsAddIncomeCommand() throws BudgieException {
        Command command = parser.parse("income 2500 /salary August pay");
        assertInstanceOf(AddIncomeCommand.class, command);
        AddIncomeCommand addCommand = (AddIncomeCommand) command;
        assertEquals(new BigDecimal("2500"), addCommand.getIncome().getAmount());
        assertEquals("salary", addCommand.getIncome().getCategory());
        assertEquals("August pay", addCommand.getIncome().getDescription());
        assertFalse(command.isExit());
    }

    @Test
    public void parse_incomeDifferentCase_returnsAddIncomeCommand() throws BudgieException {
        Command command = parser.parse("Income 100 /gift Angbao");
        assertInstanceOf(AddIncomeCommand.class, command);
        AddIncomeCommand addCommand = (AddIncomeCommand) command;
        assertEquals("Angbao", addCommand.getIncome().getDescription());
    }

    @Test
    public void parse_incomeMissingAmount_throwsIncomeMissingAmount() {
        BudgieException thrown = assertThrows(BudgieException.class, () -> parser.parse("income /salary August pay"));
        assertEquals(Parser.INCOME_MISSING_AMOUNT, thrown.getMessage());
    }

    @Test
    public void parse_incomeMissingDescription_throwsBudgieException() {
        BudgieException thrown = assertThrows(BudgieException.class, () -> parser.parse("income 2500 /salary"));
        assertEquals(Parser.INCOME_USAGE, thrown.getMessage());
    }

    @Test
    public void parse_list_returnsListCommand() throws BudgieException {
        Command command = parser.parse("list");
        assertInstanceOf(ListCommand.class, command);
        assertFalse(command.isExit());
    }

    @Test
    public void parse_listDifferentCase_returnsListCommand() throws BudgieException {
        Command command = parser.parse("LIST");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parse_validDelete_returnsDeleteCommand() throws BudgieException {
        Command command = parser.parse("delete 1");
        assertInstanceOf(DeleteCommand.class, command);
        assertEquals(1, ((DeleteCommand) command).getOneBasedIndex());
        assertFalse(command.isExit());
    }

    @Test
    public void parse_deleteMissingIndex_throwsBudgieException() {
        BudgieException thrown = assertThrows(BudgieException.class, () -> parser.parse("delete"));
        assertEquals(Parser.DELETE_USAGE, thrown.getMessage());
    }

    @Test
    public void parse_deleteZeroIndex_throwsBudgieException() {
        assertThrows(BudgieException.class, () -> parser.parse("delete 0"));
    }

    @Test
    public void parse_validFind_returnsFindCommand() throws BudgieException {
        Command command = parser.parse("find food");
        assertInstanceOf(FindCommand.class, command);
        assertEquals("food", ((FindCommand) command).getKeyword());
        assertFalse(command.isExit());
    }

    @Test
    public void parse_findTwoWords_keepsFullKeyword() throws BudgieException {
        Command command = parser.parse("find August pay");
        assertEquals("August pay", ((FindCommand) command).getKeyword());
    }

    @Test
    public void parse_summary_returnsSummaryCommand() throws BudgieException {
        Command command = parser.parse("summary");
        assertInstanceOf(SummaryCommand.class, command);
        assertFalse(command.isExit());
        assertFalse(command.modifiesData());
    }

    @Test
    public void parse_summaryDifferentCase_returnsSummaryCommand() throws BudgieException {
        Command command = parser.parse("SUMMARY");
        assertInstanceOf(SummaryCommand.class, command);
    }

    @Test
    public void parse_findMissingKeyword_throwsBudgieException() {
        BudgieException thrown = assertThrows(BudgieException.class, () -> parser.parse("find"));
        assertEquals(Parser.FIND_USAGE, thrown.getMessage());
    }
}
