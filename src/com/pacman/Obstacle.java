package com.pacman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The Obstacle class extends Rectangle, representing an obstacle in the game.
 * It defines the shape, color, size and position of the obstacle.
 * Obstacle could be either horizontal or vertical.
 */
public class Obstacle extends Rectangle {
    
    /**
     * Static value for the thickness of the obstacle.
     */
    public static double THICKNESS = 25;
    
    /**
     * Constructor for Obstacle class, sets the position, dimensions, and visual properties of the obstacle.
     *
     * @param x          The x-coordinate of the obstacle's position.
     * @param y          The y-coordinate of the obstacle's position.
     * @param orientation The orientation of the obstacle. Could be "horizontal" or "vertical".
     * @param length     The length of the obstacle, the unit is the thickness of the obstacle.
     */
    public Obstacle(double x, double y, String orientation, double length) {
        this.setX(x);
        this.setY(y);
        if (orientation.equals("horizontal")) {
            this.setHeight(Obstacle.THICKNESS);
            this.setWidth(length * Obstacle.THICKNESS);
        } else {
            this.setHeight(length * Obstacle.THICKNESS);
            this.setWidth(Obstacle.THICKNESS);
        }
        this.setFill(Color.BLUE);
        this.setStrokeWidth(3);
    }
}
