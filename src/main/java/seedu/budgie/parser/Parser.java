package seedu.budgie.parser;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.budgie.command.AddExpenseCommand;
import seedu.budgie.command.AddIncomeCommand;
import seedu.budgie.command.Command;
import seedu.budgie.command.ExitCommand;
import seedu.budgie.command.HelpCommand;
import seedu.budgie.command.ListCommand;
import seedu.budgie.command.UnknownCommand;
import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Expense;
import seedu.budgie.model.Income;

/**
 * Converts raw user input into a {@link Command}.
 */
public class Parser {

    public static final String EXPENSE_USAGE = "Expense must be: expense AMOUNT /CATEGORY DESCRIPTION\n"
            + "Example: expense 12.50 /food lunch";
    public static final String INCOME_USAGE = "Income must be: income AMOUNT /CATEGORY DESCRIPTION\n"
            + "Example: income 2500 /salary August pay";

    private static final Pattern ENTRY_ARGS = Pattern.compile("(?<amount>\\S+)\\s+/(?<category>\\S+)\\s+(?<desc>.+)");

    /**
     * Parsed amount, category, and description from an expense or income command.
     */
    private static class ParsedEntry {
        private final BigDecimal amount;
        private final String category;
        private final String description;

        private ParsedEntry(BigDecimal amount, String category, String description) {
            this.amount = amount;
            this.category = category;
            this.description = description;
        }
    }

    /**
     * Parses {@code input} into the corresponding command.
     *
     * @param input trimmed user input
     * @return a command that can be executed
     * @throws BudgieException if a recognised command has invalid arguments
     */
    public Command parse(String input) throws BudgieException {
        assert input != null : "input should not be null";
        String trimmed = input.trim();
        String[] parts = trimmed.split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (commandWord) {
            case "bye":
                return new ExitCommand();
            case "help":
                return new HelpCommand();
            case "expense":
                return parseExpense(arguments);
            case "income":
                return parseIncome(arguments);
            case "list":
                return new ListCommand();
            default:
                return new UnknownCommand(trimmed);
        }
    }

    private AddExpenseCommand parseExpense(String arguments) throws BudgieException {
        ParsedEntry entry = parseEntry(arguments, EXPENSE_USAGE);
        return new AddExpenseCommand(new Expense(entry.amount, entry.category, entry.description));
    }

    private AddIncomeCommand parseIncome(String arguments) throws BudgieException {
        ParsedEntry entry = parseEntry(arguments, INCOME_USAGE);
        return new AddIncomeCommand(new Income(entry.amount, entry.category, entry.description));
    }

    private ParsedEntry parseEntry(String arguments, String usage) throws BudgieException {
        Matcher matcher = ENTRY_ARGS.matcher(arguments.trim());
        if (!matcher.matches()) {
            throw new BudgieException(usage);
        }

        BigDecimal amount = parseAmount(matcher.group("amount"));
        String category = matcher.group("category");
        String description = matcher.group("desc").trim();
        if (description.isEmpty()) {
            throw new BudgieException(usage);
        }
        return new ParsedEntry(amount, category, description);
    }

    /**
     * Parses a money amount. Must be positive and have at most two decimal places.
     *
     * @param amountText raw amount token
     * @return parsed amount
     * @throws BudgieException if the amount is not a valid positive money value
     */
    private BigDecimal parseAmount(String amountText) throws BudgieException {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText);
        } catch (NumberFormatException e) {
            throw new BudgieException("Amount must be a positive number with up to 2 decimal places.");
        }
        if (amount.scale() > 2) {
            throw new BudgieException("Amount must be a positive number with up to 2 decimal places.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BudgieException("Amount must be a positive number with up to 2 decimal places.");
        }
        return amount;
    }
}
