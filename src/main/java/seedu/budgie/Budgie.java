package seedu.budgie;

import seedu.budgie.command.Command;
import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.parser.Parser;
import seedu.budgie.storage.Storage;
import seedu.budgie.ui.Ui;

/**
 * Entry point of the Budgie personal budget tracker.
 */
public class Budgie {

    private final Ui ui;
    private final Parser parser;
    private final Storage storage;
    private final ExpenseBook expenseBook;
    private final String loadWarning;

    /**
     * Creates a Budgie application, loading any previously saved transactions.
     */
    public Budgie() {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage();
        Storage.LoadResult loaded = storage.load();
        this.expenseBook = loaded.getExpenseBook();
        this.loadWarning = loaded.getWarningMessage();
    }

    /**
     * Runs the command loop until the user exits.
     */
    public void run() {
        ui.showWelcome();
        if (loadWarning != null) {
            ui.showMessage(loadWarning);
        }
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            if (fullCommand.isEmpty()) {
                continue;
            }
            try {
                Command command = parser.parse(fullCommand);
                String message = command.execute(expenseBook);
                if (command.modifiesData()) {
                    try {
                        storage.save(expenseBook);
                    } catch (BudgieException e) {
                        message = message + "\n" + e.getMessage();
                    }
                }
                ui.showMessage(message);
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
