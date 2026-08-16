package seedu.budgie;

import seedu.budgie.command.Command;
import seedu.budgie.exception.BudgieException;
import seedu.budgie.model.ExpenseBook;
import seedu.budgie.parser.Parser;
import seedu.budgie.storage.Storage;
import seedu.budgie.ui.Ui;

/**
 * Logic for the Budgie personal budget tracker. The CLI and GUI both use this class.
 */
public class Budgie {

    private final Parser parser;
    private final Storage storage;
    private final ExpenseBook expenseBook;
    private final String loadWarning;

    /**
     * Creates a Budgie application, loading any previously saved transactions.
     */
    public Budgie() {
        this.parser = new Parser();
        this.storage = new Storage();
        Storage.LoadResult loaded = storage.load();
        this.expenseBook = loaded.getExpenseBook();
        this.loadWarning = loaded.getWarningMessage();
    }

    /**
     * Returns the greeting, plus a load warning when the save file had skipped lines.
     *
     * @return welcome text for CLI or GUI
     */
    public String getWelcomeMessage() {
        if (loadWarning == null) {
            return Messages.WELCOME;
        }
        return Messages.WELCOME + "\n\n" + loadWarning;
    }

    /**
     * Parses and executes {@code input}, saving after commands that change data.
     *
     * @param input one command line
     * @return message to show, and whether to exit
     */
    public CommandResult getResponse(String input) {
        assert input != null : "input should not be null";
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return new CommandResult("", false);
        }
        try {
            Command command = parser.parse(trimmed);
            String message = command.execute(expenseBook);
            if (command.modifiesData()) {
                try {
                    storage.save(expenseBook);
                } catch (BudgieException e) {
                    message = message + "\n" + e.getMessage();
                }
            }
            return new CommandResult(message, command.isExit());
        } catch (BudgieException e) {
            return new CommandResult(e.getMessage(), false);
        }
    }

    /**
     * Runs the command loop on standard input until the user exits.
     */
    public void run() {
        Ui ui = new Ui();
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
            CommandResult result = getResponse(fullCommand);
            ui.showMessage(result.getMessage());
            isExit = result.isExit();
        }
        ui.close();
    }

    /**
     * Starts the CLI. Use {@link Launcher} to start the GUI.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        new Budgie().run();
    }
}
