package example;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The Main class is the entry point for the PACMAN game. It sets up the game's UI and handles user interaction.
 */
public class Main {

    /**
     * Starts the PACMAN game by setting up the game stage, drawing the maze, and registering event handlers.
     */
    public void startGame() {
        // Create a new stage for the game
        Stage gameStage = new Stage();
        gameStage.setTitle("PACMAN - The Arcade Game");

        // Create the root group and scene
        Group root = new Group();
        Scene scene = new Scene(root);
        gameStage.setScene(scene);

        // Create and add the game canvas
        Canvas canvas = new Canvas(1225, 600);
        root.getChildren().add(canvas);

        // Initialize and draw the game manager
        Manager gameManager = new Manager(root, gameStage);
        gameManager.drawMaze();

        // Register event handlers for key events
        scene.addEventHandler(KeyEvent.KEY_PRESSED, gameManager::movePacman);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, gameManager::stopPacman);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, gameManager::restartGame);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, gameManager::togglePowerUp);

        // Show the game stage
        gameStage.show();
    }

    /**
     * The main method serves as the entry point for the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        // Initialize and start the game
        Main mainGame = new Main();
        mainGame.startGame();
    }
}
