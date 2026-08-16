package seedu.budgie.exception;

/**
 * Signals a user-facing error such as an invalid command format.
 */
public class BudgieException extends Exception {

    /**
     * Creates an exception with a message that can be shown to the user.
     *
     * @param message explanation of the error
     */
    public BudgieException(String message) {
        super(message);
    }
}
