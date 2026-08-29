package seedu.budgie.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seedu.budgie.Budgie;
import seedu.budgie.CommandResult;

/**
 * Controller for the main chat window.
 */
public class MainWindow extends BorderPane {

    private static final double EXIT_DELAY_SECONDS = 1.0;

    private enum LastSpeaker {
        NONE, USER, BUDGIE
    }

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Budgie budgie;
    private LastSpeaker lastSpeaker = LastSpeaker.NONE;

    /**
     * Binds the scroll pane to the dialog list so new messages stay in view.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Connects this window to {@code budgie} and shows the greeting.
     *
     * @param budgie application logic
     */
    public void setBudgie(Budgie budgie) {
        this.budgie = budgie;
        lastSpeaker = LastSpeaker.NONE;
        addBudgieBubble(budgie.getWelcomeMessage());
    }

    /**
     * Sends the text field contents to Budgie and shows the reply.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }
        CommandResult result = budgie.getResponse(input);
        addUserBubble(input);
        addBudgieBubble(result.getMessage());
        userInput.clear();
        if (result.isExit()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.seconds(EXIT_DELAY_SECONDS));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }

    private void addUserBubble(String text) {
        boolean showSpeaker = lastSpeaker != LastSpeaker.USER;
        dialogContainer.getChildren().add(DialogBox.ofUser(text, showSpeaker));
        lastSpeaker = LastSpeaker.USER;
    }

    private void addBudgieBubble(String text) {
        boolean showSpeaker = lastSpeaker != LastSpeaker.BUDGIE;
        dialogContainer.getChildren().add(DialogBox.ofBudgie(text, showSpeaker));
        lastSpeaker = LastSpeaker.BUDGIE;
    }
}
