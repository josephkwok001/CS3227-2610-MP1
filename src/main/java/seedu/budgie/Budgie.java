package seedu.budgie;

import seedu.budgie.command.Command;
import seedu.budgie.parser.Parser;
import seedu.budgie.ui.Ui;

/**
 * Entry point of the Budgie personal budget tracker.
 */
public class Budgie {

    private final Ui ui;
    private final Parser parser;

    /**
     * Creates a Budgie application with its UI and parser collaborators.
     */
    public Budgie() {
        this.ui = new Ui();
        this.parser = new Parser();
    }

    /**
     * Runs the command loop until the user exits.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            if (fullCommand.isEmpty()) {
                continue;
            }
            Command command = parser.parse(fullCommand);
            ui.showMessage(command.execute());
            isExit = command.isExit();
        }
        ui.close();
    }

    public static void main(String[] args) {
        new Budgie().run();
    }
}
