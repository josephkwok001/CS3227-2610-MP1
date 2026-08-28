package seedu.budgie.command;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Entry;
import seedu.budgie.model.Expense;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.model.Income;
import seedu.budgie.model.MoneyFormatter;

/**
 * Shows income and expense totals, net (income minus expenses), and a breakdown by category.
 */
public class SummaryCommand implements Command {

    @Override
    public String execute(ExpenseBook expenseBook) throws BudgieException {
        if (expenseBook.totalCount() == 0) {
            return ListCommand.EMPTY_MESSAGE;
        }

        BigDecimal incomeTotal = BigDecimal.ZERO;
        BigDecimal expenseTotal = BigDecimal.ZERO;
        Map<String, BigDecimal> expenseByCategory = new LinkedHashMap<>();
        Map<String, BigDecimal> incomeByCategory = new LinkedHashMap<>();

        for (Entry entry : expenseBook.getEntries()) {
            if (entry instanceof Expense) {
                Expense expense = (Expense) entry;
                expenseTotal = expenseTotal.add(expense.getAmount());
                addToCategory(expenseByCategory, expense.getCategory(), expense.getAmount());
            } else if (entry instanceof Income) {
                Income income = (Income) entry;
                incomeTotal = incomeTotal.add(income.getAmount());
                addToCategory(incomeByCategory, income.getCategory(), income.getAmount());
            } else {
                assert false : "unknown entry type";
            }
        }

        BigDecimal net = incomeTotal.subtract(expenseTotal);
        StringBuilder result = new StringBuilder("Here is your summary:");
        result.append('\n').append("Income: ").append(MoneyFormatter.formatSignedDisplay(incomeTotal));
        result.append('\n').append("Expenses: ").append(MoneyFormatter.formatSignedDisplay(expenseTotal));
        result.append('\n').append("Net: ").append(MoneyFormatter.formatSignedDisplay(net));
        appendCategorySection(result, "Expenses by category:", expenseByCategory);
        appendCategorySection(result, "Income by category:", incomeByCategory);
        return result.toString();
    }

    @Override
    public boolean isExit() {
        return false;
    }

    private static void addToCategory(Map<String, BigDecimal> totals, String category, BigDecimal amount) {
        totals.merge(category, amount, BigDecimal::add);
    }

    private static void appendCategorySection(StringBuilder result, String heading,
            Map<String, BigDecimal> byCategory) {
        if (byCategory.isEmpty()) {
            return;
        }
        result.append("\n\n").append(heading);
        for (Map.Entry<String, BigDecimal> line : byCategory.entrySet()) {
            result.append('\n').append("  /").append(line.getKey()).append(": ")
                    .append(MoneyFormatter.formatSignedDisplay(line.getValue()));
        }
    }
}
