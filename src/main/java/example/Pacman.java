package example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The PACMAN class extends a Circle, representing the PACMAN character in the game.
 * Controlled by the player and navigated across the maze.
 * It defines the shape, color, and size of the character, as well as its initial position.
 */
public class Pacman extends Circle {

    /**
     * Constructor for PACMAN class, sets the initial position and visual properties of the character.
     *
     * @param x The x-coordinate of the example.Pacman character's initial position.
     * @param y The y-coordinate of the example.Pacman character's initial position.
     */
    public Pacman(double x, double y) {
        // Set the x-coordinate of PACMAN's center
        this.setCenterX(x);
        // Set the y-coordinate of PACMAN's center
        this.setCenterY(y);
        // Set PACMAN's radius
        this.setRadius(25);
        // Set PACMAN's color
        this.setFill(Color.YELLOW);
    }
}
