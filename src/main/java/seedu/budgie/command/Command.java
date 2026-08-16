package seedu.budgie.command;

import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;

/**
 * Represents a user command that can be executed by Budgie.
 */
public interface Command {

    /**
     * Executes this command and returns the message to show the user.
     *
     * @param expenseBook in-memory expenses for this session
     * @return user-facing result of the command
     * @throws BudgieException if the command cannot be completed with the current data
     */
    String execute(ExpenseBook expenseBook) throws BudgieException;

    /**
     * Returns whether executing this command should terminate the application.
     *
     * @return {@code true} if the session should end
     */
    boolean isExit();
}
