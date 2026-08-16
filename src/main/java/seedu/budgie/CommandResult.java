package seedu.budgie;

/**
 * User-facing result of handling one command line, including whether the session should end.
 */
public class CommandResult {

    private final String message;
    private final boolean isExit;

    /**
     * Creates a result to show the user.
     *
     * @param message text to display
     * @param isExit whether the session should end
     */
    public CommandResult(String message, boolean isExit) {
        this.message = message;
        this.isExit = isExit;
    }

    public String getMessage() {
        return message;
    }

    public boolean isExit() {
        return isExit;
    }
}
