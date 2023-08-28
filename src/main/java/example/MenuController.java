package example;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.fxml.FXML;

/**
 * This is the MenuController class responsible for managing the menu UI and user interactions in the game.
 */
public class MenuController {

    // Stage to manage the game window
    private Stage gameStage;

    // Root group node for the game
    Group root = new Group();

    // Button to start the game
    @FXML
    private Button playButton;

    // Button to select the game level
    @FXML
    private Button levelSelectButton;

    // Text field to input the player name
    @FXML
    private TextField playerNameInput;

    /**
     * Initializes the MenuController by setting up listeners for text input and keyboard events.
     */
    @FXML
    public void initialize() {
        // Add listener to player name text field to validate and transform the text
        playerNameInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 3) {
                    playerNameInput.setText(oldValue);
                } else {
                    playerNameInput.setText(newValue.toUpperCase());
                }
            }
        });

        // Bind the filterInput method to the playerNameInput field
        playerNameInput.addEventFilter(KeyEvent.KEY_TYPED, this::filterInput);
    }

    /**
     * Handler to switch to the game screen.
     *
     * @param event The ActionEvent triggering this handler.
     */
    @FXML
    private void switchToGame(ActionEvent event) {
        String playerName = playerNameInput.getText();  // Retrieve the player name
        Manager manager = new Manager(root, gameStage); // Create the Manager instance
        manager.setPlayerName(playerName);              // Set the player name
        Stage currentStage = (Stage) playButton.getScene().getWindow(); // Get the current stage
        MainFX.launchGame(currentStage);                // Launch the game
        System.out.println("Prepare to enter the game...");
    }

    /**
     * Handler to switch to the level selection screen.
     *
     * @param event The ActionEvent triggering this handler.
     */
    @FXML
    public void selectLevel(ActionEvent event) {
        Stage currentStage = (Stage) levelSelectButton.getScene().getWindow(); // Get the current stage
        MainFX.launchLevelSelector(currentStage); // Launch the level selection screen
        System.out.println("Select the level difficulty you would like...");
    }

    /**
     * Filters the input for the playerNameInput text field.
     *
     * @param keyEvent The KeyEvent object representing the keyboard event.
     */
    public void filterInput(KeyEvent keyEvent) {
        char charTyped = keyEvent.getCharacter().charAt(0); // Get the typed character

        // Check if the typed character is a letter or digit
        if (!Character.isLetterOrDigit(charTyped)) {
            keyEvent.consume(); // If not, consume the event
        }

        // Limit the text field to 4 characters
        TextField source = (TextField) keyEvent.getSource();
        if (source.getText().length() >= 4) {
            keyEvent.consume(); // If more than 4 characters, consume the event
        }
    }

}
