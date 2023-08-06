package example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * example.Main class for the PACMAN GAME
 * here the program sets up the game's UI and handles user interaction.
 */
public class Main extends Application {

    /**
     * Entry point of the JavaFX application.
     * Sets up the scene, game manager, and key event handlers.
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set
     * @throws Exception if any error occurs
     */

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle( "PACMAN - The Arcade Game" );

        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene( scene );

        Canvas canvas = new Canvas( 1225, 600 );
        root.getChildren().add( canvas );

        Manager gameManager = new Manager(root);
        gameManager.drawMaze();

        // Assign event handlers for key presses and releases
        scene.addEventHandler(KeyEvent.KEY_PRESSED, gameManager::movePacman);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, gameManager::stopPacman);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, gameManager::restartGame);

        stage.show();
    }

    /**
     * example.Main method that launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}