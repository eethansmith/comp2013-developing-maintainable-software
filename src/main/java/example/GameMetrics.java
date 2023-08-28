package example;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * The GameMetrics class is responsible for maintaining and displaying
 * game-related metrics like score, lives, and power-up prompts.
 * It utilizes JavaFX to render text elements for these metrics.
 */
public class GameMetrics {

    /** Text element for displaying the current score. */
    private Text score;

    /** Text element for displaying the current lives. */
    private Text lives;

    /** Text element for displaying the power-up prompt. */
    private Text powerUpPrompt;

    /** The root node to which all metrics-related elements are added. */
    private Group root;

    /**
     * Initializes the GameMetrics object with default values and adds it to the provided root group.
     *
     * @param root The root group to which game metric elements are added.
     */
    public GameMetrics(Group root) {
        this.root = root;
        this.score = initializeText("SCORE: 0",
                Obstacle.THICKNESS * 1, Obstacle.THICKNESS * 27.5,
                Color.GOLD, Font.font("Arial Black", 50));
        this.lives = initializeText("LIVES: 3",
                Obstacle.THICKNESS * 15, Obstacle.THICKNESS * 27.5,
                Color.GOLD, Font.font("Arial Black", 50));
        this.powerUpPrompt = initializeText("Press SPACE to use POWERUP",
                Obstacle.THICKNESS * 25, Obstacle.THICKNESS * 27,
                Color.RED, Font.font("Arial Black", 35));

        root.getChildren().addAll(score, lives, powerUpPrompt);
    }

    /**
     * Initializes a text element with the given properties.
     *
     * @param content The content of the text.
     * @param x The x-coordinate for the text.
     * @param y The y-coordinate for the text.
     * @param fill The fill color for the text.
     * @param font The font to be used for the text.
     * @return The initialized text element.
     */
    private Text initializeText(String content, double x, double y, Color fill, Font font) {
        Text text = new Text(x, y, content);
        text.setFill(fill);
        text.setFont(font);
        return text;
    }

    /**
     * Updates the displayed score.
     *
     * @param newScore The new score to display.
     */
    public void updateScore(int newScore) {
        score.setText("SCORE: " + newScore);
    }

    /**
     * Updates the displayed number of lives.
     *
     * @param newLives The new number of lives to display.
     */
    public void updateLives(int newLives) {
        lives.setText("LIVES: " + newLives);
    }

    /**
     * Moves the power-up text off the screen.
     */
    public void movePowerUpTextOffScreen() {
        powerUpPrompt.setY(1225 + 50);
    }

    /**
     * Resets the position of the power-up text to its original location.
     */
    public void resetPowerUpTextPosition() {
        powerUpPrompt.setY(Obstacle.THICKNESS * 18);  // Resetting to original position
    }

    /**
     * Updates the text displayed for power-ups.
     *
     * @param newRoundText The new text to display for power-ups.
     */
    public void updateRoundText(String newRoundText) {
        powerUpPrompt.setText(newRoundText);
    }

    /**
     * Removes all text elements from the root node.
     */
    public void removeTextsFromRoot() {
        root.getChildren().removeAll(score, lives, powerUpPrompt);  // Include powerUpPrompt
    }

    /**
     * Adds all text elements back to the root node.
     */
    public void addTextsToRoot() {
        root.getChildren().addAll(score, lives, powerUpPrompt);  // Include powerUpPrompt
    }
}
