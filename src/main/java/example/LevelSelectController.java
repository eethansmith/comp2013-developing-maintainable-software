package example; // Consider changing this to example.controllers for better organization

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXML;

/**
 * The LevelSelectController class is responsible for handling user interactions on the Level Select screen.
 * It provides methods for selecting either the 'Normal' or 'Hard' level of the game.
 */
public class LevelSelectController {

    @FXML
    private Button normalLevelButton;

    @FXML
    private Button hardLevelButton;

    /**
     * This method is called upon the FXML file loading. Any necessary initializations should be done here.
     */
    @FXML
    public void initialize() {
        // Initialize your controller here if needed
    }

    /**
     * Sets the game's difficulty level to 'Normal' and returns to the main menu.
     *
     * @param event The ActionEvent object representing the button click event.
     */
    @FXML
    public void selectNormalLevel(ActionEvent event) {
        Manager.setLevel("normal"); // Set the level in your manager class to 'normal'

        // Return to the main menu
        Stage currentStage = (Stage) normalLevelButton.getScene().getWindow();
        MainFX.launchMainMenu(currentStage);
    }

    /**
     * Sets the game's difficulty level to 'Hard' and returns to the main menu.
     *
     * @param event The ActionEvent object representing the button click event.
     */
    @FXML
    public void selectHardLevel(ActionEvent event) {
        Manager.setLevel("hard"); // Set the level in your manager class to 'hard'

        // Return to the main menu
        Stage currentStage = (Stage) hardLevelButton.getScene().getWindow();
        MainFX.launchMainMenu(currentStage);
    }
}
