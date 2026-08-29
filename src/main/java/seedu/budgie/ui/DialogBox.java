package seedu.budgie.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * One chat bubble in the Budgie window.
 */
public class DialogBox extends HBox {

    private static final double BUBBLE_MAX_WIDTH = 400.0;
    private static final double SPACING = 8.0;

    /**
     * Creates a dialog aligned for either the user or Budgie.
     *
     * @param speaker short name shown above the message
     * @param text message body
     * @param isUser {@code true} to align on the right
     * @param showSpeaker whether to show the speaker label (hidden for consecutive same speaker)
     */
    public DialogBox(String speaker, String text, boolean isUser, boolean showSpeaker) {
        VBox bubble = new VBox();
        bubble.setSpacing(SPACING / 2);
        if (showSpeaker) {
            Label speakerLabel = new Label(speaker);
            speakerLabel.getStyleClass().add("dialog-speaker");
            bubble.getChildren().add(speakerLabel);
        }
        bubble.getChildren().add(createMessageBody(text));

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
     * @param showSpeaker whether to show the "You" label
     * @return dialog node
     */
    public static DialogBox ofUser(String text, boolean showSpeaker) {
        return new DialogBox("You", text, true, showSpeaker);
    }

    /**
     * Returns a user (right-aligned) dialog with the speaker label shown.
     *
     * @param text user input
     * @return dialog node
     */
    public static DialogBox ofUser(String text) {
        return ofUser(text, true);
    }

    /**
     * Returns a Budgie (left-aligned) dialog.
     *
     * @param text reply
     * @param showSpeaker whether to show the "Budgie" label
     * @return dialog node
     */
    public static DialogBox ofBudgie(String text, boolean showSpeaker) {
        return new DialogBox("Budgie", text, false, showSpeaker);
    }

    /**
     * Returns a Budgie (left-aligned) dialog with the speaker label shown.
     *
     * @param text reply
     * @return dialog node
     */
    public static DialogBox ofBudgie(String text) {
        return ofBudgie(text, true);
    }

    private static Node createMessageBody(String text) {
        if (!text.contains("\n")) {
            Label messageLabel = new Label(text);
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(BUBBLE_MAX_WIDTH);
            messageLabel.getStyleClass().add("dialog-message");
            return messageLabel;
        }

        TextFlow flow = new TextFlow();
        flow.setMaxWidth(BUBBLE_MAX_WIDTH);
        flow.getStyleClass().add("dialog-message-flow");
        String[] lines = text.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                flow.getChildren().add(new Text("\n"));
            }
            appendLineToFlow(flow, lines[i]);
        }
        return flow;
    }

    private static void appendLineToFlow(TextFlow flow, String line) {
        if (line.contains(" — ")) {
            int separator = line.indexOf(" — ");
            flow.getChildren().add(styledText(line.substring(0, separator), "dialog-message-command"));
            flow.getChildren().add(styledText(" — ", "dialog-message-separator"));
            flow.getChildren().add(styledText(line.substring(separator + 3), "dialog-message"));
            return;
        }
        Text lineText = styledText(line, isStructuredLine(line) ? "dialog-message-command" : "dialog-message");
        flow.getChildren().add(lineText);
    }

    private static Text styledText(String content, String styleClass) {
        Text text = new Text(content);
        text.getStyleClass().add(styleClass);
        return text;
    }

    private static boolean isStructuredLine(String line) {
        return line.matches("^\\d+\\. .*")
                || (line.startsWith("  /") && line.contains(":"));
    }
}
