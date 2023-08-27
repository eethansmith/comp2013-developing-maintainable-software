package example;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Main class for the PACMAN GAME
 * here the program sets up the game's UI and handles user interaction.
 */
public class Main {

    /**
     * Method to start the PACMAN game.
     */
    public void startGame() {
        Stage gameStage = new Stage();
        gameStage.setTitle("PACMAN - The Arcade Game");

        Group root = new Group();
        Scene scene = new Scene(root);
        gameStage.setScene(scene);

        Canvas canvas = new Canvas(1225, 600);
        root.getChildren().add(canvas);

        Manager gameManager = new Manager(root, gameStage);
        gameManager.drawMaze();

        // Assign event handlers for key presses and releases
        scene.addEventHandler(KeyEvent.KEY_PRESSED, gameManager::movePacman);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, gameManager::stopPacman);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, gameManager::restartGame);

        gameStage.show();
    }

    /**
     * Main method that launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Main mainGame = new Main();
        mainGame.startGame();
    }
}
