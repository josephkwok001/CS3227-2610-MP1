package seedu.budgie;

import seedu.budgie.command.Command;
import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.parser.Parser;
import seedu.budgie.ui.Ui;

/**
 * Entry point of the Budgie personal budget tracker.
 */
public class Budgie {

    private final Ui ui;
    private final Parser parser;
    private final ExpenseBook expenseBook;

    /**
     * Creates a Budgie application with its UI, parser, and in-memory expense book.
     */
    public Budgie() {
        this.ui = new Ui();
        this.parser = new Parser();
        this.expenseBook = new ExpenseBook();
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
            try {
                Command command = parser.parse(fullCommand);
                ui.showMessage(command.execute(expenseBook));
                isExit = command.isExit();
            } catch (BudgieException e) {
                ui.showMessage(e.getMessage());
            }
        }
        ui.close();
    }

    public static void main(String[] args) {
        new Budgie().run();
    }
}
