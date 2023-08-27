package example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        primaryStage.setTitle("PACMAN ARCADE - MAIN MENU");

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    public static void launchMainMenu(Stage stage) {
        try {
            Parent root = FXMLLoader.load(MainFX.class.getResource("MainMenu.fxml"));
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void launchLevelSelector(Stage stage) {
        try {
            Parent root = FXMLLoader.load(MainFX.class.getResource("LevelSelect.fxml"));
            stage.setScene(new Scene(root, 1000, 600));  // adjust size if necessary
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void launchGame(Stage currentStage) {
        Platform.runLater(() -> {
            Main game = new Main();
            game.startGame();
            currentStage.hide();
        });
    }
}
