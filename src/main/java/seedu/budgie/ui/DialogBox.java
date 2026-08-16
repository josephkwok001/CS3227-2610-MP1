package seedu.budgie.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * One chat bubble in the Budgie window.
 */
public class DialogBox extends HBox {

    private static final double BUBBLE_MAX_WIDTH = 280.0;
    private static final double SPACING = 8.0;

    /**
     * Creates a dialog aligned for either the user or Budgie.
     *
     * @param speaker short name shown above the message
     * @param text message body
     * @param isUser {@code true} to align on the right
     */
    public DialogBox(String speaker, String text, boolean isUser) {
        Label speakerLabel = new Label(speaker);
        speakerLabel.getStyleClass().add("dialog-speaker");
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(BUBBLE_MAX_WIDTH);
        messageLabel.getStyleClass().add("dialog-message");

        VBox bubble = new VBox(speakerLabel, messageLabel);
        bubble.setSpacing(SPACING / 2);
        if (isUser) {
            bubble.getStyleClass().add("user-bubble");
            setAlignment(Pos.TOP_RIGHT);
        } else {
            bubble.getStyleClass().add("budgie-bubble");
            setAlignment(Pos.TOP_LEFT);
        }
        getChildren().add(bubble);
        setSpacing(SPACING);
    }

    /**
     * Returns a user (right-aligned) dialog.
     *
     * @param text user input
     * @return dialog node
     */
    public static DialogBox ofUser(String text) {
        return new DialogBox("You", text, true);
    }

    /**
     * Returns a Budgie (left-aligned) dialog.
     *
     * @param text reply
     * @return dialog node
     */
    public static DialogBox ofBudgie(String text) {
        return new DialogBox("Budgie", text, false);
    }
}
