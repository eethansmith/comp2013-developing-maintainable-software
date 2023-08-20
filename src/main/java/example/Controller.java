package example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button playButton;

    @FXML
    private void switchToGame(ActionEvent event) {
        Stage currentStage = (Stage) playButton.getScene().getWindow();
        MainFX.launchGame(currentStage);
        System.out.println("Prepare to enter the game...");
    }

    public void selectLevel(MouseEvent mouseEvent) {
        System.out.println("Level Select Clicked *Click*...");
    }
}
