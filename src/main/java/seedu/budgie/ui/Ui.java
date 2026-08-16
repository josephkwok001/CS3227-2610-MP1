package seedu.budgie.ui;

import java.util.Scanner;

import seedu.budgie.Messages;

/**
 * Handles reading user input and printing messages to the console.
 */
public class Ui {

    private static final String LINE = "____________________________________________________________";
    private static final String LOGO =
            " ____            _       _      \n"
            + "|  _ \\          | |     (_)     \n"
            + "| |_) |_   _  __| | __ _ _  ___ \n"
            + "|  _ <| | | |/ _` |/ _` | |/ _ \\\n"
            + "| |_) | |_| | (_| | (_| | |  __/\n"
            + "|____/ \\__,_|\\__,_|\\__, |_|\\___|\n"
            + "                    __/ |       \n"
            + "                   |___/        ";

    private final Scanner scanner;

    /**
     * Creates a UI that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows the welcome banner and greeting.
     */
    public void showWelcome() {
        showMessage(LOGO + "\n" + Messages.WELCOME);
    }

    /**
     * Reads the next command line from the user, or an empty string if input has ended.
     *
     * @return trimmed user input
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return "";
        }
        return scanner.nextLine().trim();
    }

    /**
     * Prints {@code message} wrapped in divider lines.
     *
     * @param message text to display
     */
    public void showMessage(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Releases the input scanner.
     */
    public void close() {
        scanner.close();
    }
}
