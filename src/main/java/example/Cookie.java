package example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents a example.Cookie (or score) in the PACMAN game
 * when the PACMAN goes over the cookie, the player receives a score
 */
public class Cookie extends Circle {

    /**
     * The point value of this example.Cookie.
     */
    private final int value;

    /**
     * Creates a new example.Cookie at the specified coordinates.
     *
     * @param x the x-coordinate of the center of the cookie
     * @param y the y-coordinate of the center of the cookie
     */
    public Cookie(double x, double y) {
        this.value = 5;
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(12.5);
        this.setFill(Color.GOLD);
    }

    /**
     * Gets the point value of this example.Cookie.
     *
     * @return the point value of this example.Cookie
     */
    public int getValue() {
        return value;
    }

    /**
     * Hides the example.Cookie.
     */
    public void hide() {
        this.setVisible(false);
    }

    /**
     * Shows the example.Cookie.
     */
    public void show() {
        this.setVisible(true);
    }

}