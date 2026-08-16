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
import seedu.budgie.command.ExitCommand;
import seedu.budgie.command.HelpCommand;
import seedu.budgie.command.UnknownCommand;
import seedu.budgie.exception.BudgieException;

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
        Command command = parser.parse("list");
        assertInstanceOf(UnknownCommand.class, command);
        assertFalse(command.isExit());
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
    public void parse_expenseMissingDescription_throwsBudgieException() {
        BudgieException thrown = assertThrows(BudgieException.class, () -> parser.parse("expense 12.50 /food"));
        assertEquals(Parser.EXPENSE_USAGE, thrown.getMessage());
    }

    @Test
    public void parse_expenseMissingSlashCategory_throwsBudgieException() {
        assertThrows(BudgieException.class, () -> parser.parse("expense 12.50 food lunch"));
    }

    @Test
    public void parse_expenseZeroAmount_throwsBudgieException() {
        assertThrows(BudgieException.class, () -> parser.parse("expense 0 /food lunch"));
    }

    @Test
    public void parse_expenseNegativeAmount_throwsBudgieException() {
        assertThrows(BudgieException.class, () -> parser.parse("expense -1 /food lunch"));
    }

    @Test
    public void parse_expenseTooManyDecimals_throwsBudgieException() {
        assertThrows(BudgieException.class, () -> parser.parse("expense 12.555 /food lunch"));
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
    public void parse_incomeMissingDescription_throwsBudgieException() {
        BudgieException thrown = assertThrows(BudgieException.class, () -> parser.parse("income 2500 /salary"));
        assertEquals(Parser.INCOME_USAGE, thrown.getMessage());
    }

    @Test
    public void parse_incomeZeroAmount_throwsBudgieException() {
        assertThrows(BudgieException.class, () -> parser.parse("income 0 /salary August pay"));
    }
}
