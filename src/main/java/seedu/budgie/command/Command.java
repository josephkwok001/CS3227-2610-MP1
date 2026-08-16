package seedu.budgie.command;

/**
 * Represents a user command that can be executed by Budgie.
 */
public interface Command {

    /**
     * Executes this command and returns the message to show the user.
     *
     * @return user-facing result of the command
     */
    String execute();

    /**
     * Returns whether executing this command should terminate the application.
     *
     * @return {@code true} if the session should end
     */
    boolean isExit();
}
