package seedu.budgie.parser;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static final String EXPENSE_MISSING_AMOUNT = "Expense is missing an amount.\n"
            + "Example: expense 12.50 /food lunch";
    public static final String INCOME_MISSING_AMOUNT = "Income is missing an amount.\n"
            + "Example: income 2500 /salary August pay";
    public static final String AMOUNT_NEGATIVE = "Amount cannot be negative.";
    public static final String AMOUNT_INVALID = "Amount must be a positive number with up to 2 decimal places.";
    public static final String DELETE_USAGE = "Delete must be: delete INDEX\n"
            + "Example: delete 1\n"
            + "INDEX is the number shown by list.";
    public static final String FIND_USAGE = "Find must be: find KEYWORD\n"
            + "Example: find food\n"
            + "KEYWORD matches category, description, or amount.";

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
            case "delete":
                return parseDelete(arguments);
            case "find":
                return parseFind(arguments);
            case "summary":
                return new SummaryCommand();
            default:
                return new UnknownCommand(trimmed);
        }
    }

    private AddExpenseCommand parseExpense(String arguments) throws BudgieException {
        ParsedEntry entry = parseEntry(arguments, EXPENSE_USAGE, EXPENSE_MISSING_AMOUNT);
        return new AddExpenseCommand(new Expense(entry.amount, entry.category, entry.description));
    }

    private AddIncomeCommand parseIncome(String arguments) throws BudgieException {
        ParsedEntry entry = parseEntry(arguments, INCOME_USAGE, INCOME_MISSING_AMOUNT);
        return new AddIncomeCommand(new Income(entry.amount, entry.category, entry.description));
    }

    private DeleteCommand parseDelete(String arguments) throws BudgieException {
        String trimmedArgs = arguments.trim();
        if (trimmedArgs.isEmpty() || trimmedArgs.split("\\s+").length != 1) {
            throw new BudgieException(DELETE_USAGE);
        }
        int index;
        try {
            index = Integer.parseInt(trimmedArgs);
        } catch (NumberFormatException e) {
            throw new BudgieException(DELETE_USAGE);
        }
        if (index < 1) {
            throw new BudgieException(DELETE_USAGE);
        }
        return new DeleteCommand(index);
    }

    private FindCommand parseFind(String arguments) throws BudgieException {
        String trimmedArgs = arguments.trim();
        if (trimmedArgs.isEmpty()) {
            throw new BudgieException(FIND_USAGE);
        }
        return new FindCommand(trimmedArgs);
    }

    private ParsedEntry parseEntry(String arguments, String usage, String missingAmountMessage)
            throws BudgieException {
        if (isMissingAmount(arguments)) {
            throw new BudgieException(missingAmountMessage);
        }
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
            throw new BudgieException(AMOUNT_INVALID);
        }
        if (amount.signum() < 0) {
            throw new BudgieException(AMOUNT_NEGATIVE);
        }
        if (amount.scale() > 2) {
            throw new BudgieException(AMOUNT_INVALID);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BudgieException(AMOUNT_INVALID);
        }
        return amount;
    }

    /**
     * Returns whether {@code arguments} has no amount token (empty, or starts with {@code /category}).
     */
    private boolean isMissingAmount(String arguments) {
        String trimmed = arguments.trim();
        if (trimmed.isEmpty()) {
            return true;
        }
        String firstToken = trimmed.split("\\s+", 2)[0];
        return firstToken.startsWith("/");
    }
}
