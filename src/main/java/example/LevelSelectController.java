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


public class LevelSelectController {

    private Stage gameStage;
    Group root = new Group();

    @FXML
    private Button normalLevelButton;

    @FXML
    private Button hardLevelButton;

    @FXML
    public void initialize() {
        // Initialize your controller here if needed
    }

    @FXML
    public void selectNormalLevel(ActionEvent event) {
        // Set the level in your manager class to normal
        Manager.setLevel("normal");

        // Return to the main menu
        Stage currentStage = (Stage) normalLevelButton.getScene().getWindow();
        MainFX.launchMainMenu(currentStage);
    }

    @FXML
    public void selectHardLevel(ActionEvent event) {
        // Set the level in your manager class to hard
        Manager.setLevel("hard");

        // Return to the main menu
        Stage currentStage = (Stage) hardLevelButton.getScene().getWindow();
        MainFX.launchMainMenu(currentStage);
    }
}
