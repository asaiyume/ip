package zbot.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import zbot.Zbot;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Zbot zbot;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image zbotImage = new Image(this.getClass().getResourceAsStream("/images/DaZbot.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Zbot instance */
    public void setZbot(Zbot z) {
        zbot = z;
        String welcomeMsg = "Hello! I'm Zbot\nWhat can I do for you?";
        dialogContainer.getChildren().addAll(
                DialogBox.getZbotDialog(welcomeMsg, zbotImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Zbot's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = zbot.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getZbotDialog(response, zbotImage)
        );
        userInput.clear();
    }
}

