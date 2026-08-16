package seedu.budgie.command;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.Entry;
import seedu.budgie.model.ExpenseBook;

/**
 * Shows transactions whose category, description, or amount matches a keyword.
 */
public class FindCommand implements Command {

    public static final String NO_MATCH_MESSAGE = "No matching transactions found.";

    private final String keyword;

    /**
     * Creates a find command for {@code keyword}.
     *
     * @param keyword search text
     */
    public FindCommand(String keyword) {
        assert keyword != null && !keyword.isBlank() : "keyword should not be blank";
        this.keyword = keyword;
    }

    @Override
    public String execute(ExpenseBook expenseBook) throws BudgieException {
        StringBuilder result = new StringBuilder("Here are the matching transactions:");
        int shown = 0;
        int index = 1;
        for (Entry entry : expenseBook.getEntries()) {
            if (entry.matchesKeyword(keyword)) {
                result.append('\n').append(index).append(". ").append(entry.toListLine());
                shown++;
            }
            index++;
        }
        if (shown == 0) {
            return NO_MATCH_MESSAGE;
        }
        return result.toString();
    }

    @Override
    public boolean isExit() {
        return false;
    }

    public String getKeyword() {
        return keyword;
    }
}
