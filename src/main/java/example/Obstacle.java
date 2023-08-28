package example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The Obstacle class extends Rectangle, representing an obstacle in the game.
 * It defines the shape, color, size, and position of the obstacle.
 * Obstacles could be either horizontal or vertical.
 */
public class Obstacle extends Rectangle {

    /**
     * Static value for the thickness of the obstacle.
     */
    public static double THICKNESS = 25;

    // Reference to the Manager class to get game configurations like difficulty level
    private Manager manager;

    /**
     * Constructor for Obstacle class.
     * Sets the position, dimensions, and visual properties of the obstacle.
     *
     * @param x           The x-coordinate of the obstacle's position.
     * @param y           The y-coordinate of the obstacle's position.
     * @param orientation The orientation of the obstacle. Could be "horizontal" or "vertical".
     * @param length      The length of the obstacle, in terms of the unit THICKNESS.
     */
    public Obstacle(double x, double y, String orientation, double length) {
        // Initialize the manager reference (it seems the manager should be passed as an argument to the constructor)
        this.manager = manager;

        // Get the difficulty level from the manager
        String difficulty = manager.getLevel();

        // Set x-coordinate of obstacle's position
        this.setX(x);

        // Set y-coordinate of obstacle's position
        this.setY(y);

        // Determine the dimensions of the obstacle based on its orientation
        if (orientation.equals("horizontal")) {
            // Set height to THICKNESS and width to length multiplied by THICKNESS
            this.setHeight(Obstacle.THICKNESS);
            this.setWidth(length * Obstacle.THICKNESS);
        } else {
            // Set height to length multiplied by THICKNESS and width to THICKNESS
            this.setHeight(length * Obstacle.THICKNESS);
            this.setWidth(Obstacle.THICKNESS);
        }

        // Set the stroke width of the obstacle outline
        this.setStrokeWidth(3);

        // Apply color based on the difficulty level
        if (difficulty != null) {
            if (difficulty.equals("hard")) {
                // Set fill color of the obstacle to DARKRED for hard difficulty
                this.setFill(Color.DARKRED);
            } else if (difficulty.equals("normal")) {
                // Set fill color of the obstacle to BLUE for normal difficulty
                this.setFill(Color.BLUE);
            }
        } else {
            // Default fill color if difficulty is null
            this.setFill(Color.BLUE);
        }
    }
}
