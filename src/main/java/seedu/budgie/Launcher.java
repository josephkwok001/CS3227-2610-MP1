package seedu.budgie;

import javafx.application.Application;

/**
 * Entry point of the JavaFX GUI.
 * <p>
 * {@link MainApp} cannot be the JAR main class because it extends {@link Application}.
 * Java would then expect JavaFX to be a named module. This launcher avoids that check.
 */
public class Launcher {

    /**
     * Starts the GUI.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
