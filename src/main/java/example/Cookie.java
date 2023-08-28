package example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The Cookie class represents a cookie (or score) in the PACMAN game.
 * It extends the Circle class to inherit graphical properties.
 * When PACMAN collides with a cookie, the player's score is incremented.
 */
public class Cookie extends Circle {

    /** The point value associated with collecting this cookie. */
    private final int value;

    /**
     * Constructs a new Cookie at the specified x and y coordinates.
     *
     * @param x The x-coordinate for the center of the cookie.
     * @param y The y-coordinate for the center of the cookie.
     */
    public Cookie(double x, double y) {
        this.value = 5;  // Initializing point value for this cookie
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(12.5);
        this.setFill(Color.GOLD);
    }

    /**
     * Returns the point value associated with this cookie.
     *
     * @return The point value of this cookie.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the visibility of this cookie to false, effectively "hiding" it.
     */
    public void hide() {
        this.setVisible(false);
    }

    /**
     * Sets the visibility of this cookie to true, effectively "showing" it.
     */
    public void show() {
        this.setVisible(true);
    }
}
