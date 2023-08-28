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

public class MenuController {

    private Stage gameStage;
    Group root = new Group();

    @FXML
    private Button playButton;

    @FXML
    private Button levelSelectButton;

    @FXML
    private TextField playerNameInput;

    @FXML
    public void initialize() {
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

        // Binding the filterInput method to the playerNameInput
        playerNameInput.addEventFilter(KeyEvent.KEY_TYPED, this::filterInput);

    }

    @FXML
    private void switchToGame(ActionEvent event) {
        String playerName = playerNameInput.getText();
        Manager manager = new Manager(root, gameStage);
        manager.setPlayerName(playerName);
        Stage currentStage = (Stage) playButton.getScene().getWindow();
        MainFX.launchGame(currentStage);
        System.out.println("Prepare to enter the game...");
    }

    @FXML
    public void selectLevel(ActionEvent event) {
        Stage currentStage = (Stage) levelSelectButton.getScene().getWindow();
        MainFX.launchLevelSelector(currentStage);
        System.out.println("Select the level difficulty you would like...");
    }

    public void filterInput(KeyEvent keyEvent) {
        char charTyped = keyEvent.getCharacter().charAt(0);

        // If the character is not a letter or number, consume (discard) the event.
        if (!Character.isLetterOrDigit(charTyped)) {
            keyEvent.consume();
        }

        // Limit to only 4 characters.
        TextField source = (TextField) keyEvent.getSource();
        if (source.getText().length() >= 4) {
            keyEvent.consume();
        }
    }

}