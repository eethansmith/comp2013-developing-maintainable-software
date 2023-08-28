package example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The MainFX class serves as the launching point for the PACMAN Arcade game application.
 * It handles scene transitions and main menu setups.
 */
public class MainFX extends Application {

    /**
     * The main method that serves as the entry point for the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the main menu of the PACMAN game.
     *
     * @param primaryStage The primary stage for this application.
     * @throws IOException If the fxml file for the main menu is not found.
     */
    @Override
    public void start (Stage primaryStage) throws IOException {
        primaryStage.setTitle("PACMAN ARCADE - MAIN MENU");
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    /**
     * Launches the main menu.
     *
     * @param stage The stage on which the main menu will be launched.
     */
    public static void launchMainMenu(Stage stage) {
        try {
            Parent root = FXMLLoader.load(MainFX.class.getResource("MainMenu.fxml"));
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }
    }

    /**
     * Launches the level selector.
     *
     * @param stage The stage on which the level selector will be launched.
     */
    public static void launchLevelSelector(Stage stage) {
        try {
            Parent root = FXMLLoader.load(MainFX.class.getResource("LevelSelect.fxml"));
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }
    }

    /**
     * Launches the main game and hides the current stage.
     *
     * @param currentStage The current stage that will be hidden.
     */
    public static void launchGame(Stage currentStage) {
        Platform.runLater(() -> {
            Main game = new Main();
            game.startGame();
            currentStage.hide();
        });
    }
}
