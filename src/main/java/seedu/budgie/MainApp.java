package seedu.budgie;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.budgie.ui.MainWindow;

/**
 * JavaFX application that opens the Budgie chat window.
 */
public class MainApp extends Application {

    private static final double MIN_WINDOW_WIDTH = 420.0;
    private static final double MIN_WINDOW_HEIGHT = 500.0;

    /**
     * Loads the main window and shows it.
     *
     * @param stage primary window
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Budgie");
            stage.setMinWidth(MIN_WINDOW_WIDTH);
            stage.setMinHeight(MIN_WINDOW_HEIGHT);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setBudgie(new Budgie());
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load the Budgie window.", e);
        }
    }
}
