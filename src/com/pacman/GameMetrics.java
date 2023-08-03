package com.pacman;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * The Score class represents the player's score and lives count in the game.
 * It displays the current score and lives count on the game screen.
 */
public class Score {

    /**
     * Represents the player's score text.
     * It's private to ensure encapsulation.
     */
    private Text score;

    /**
     * Represents the player's lives text.
     * It's private to ensure encapsulation.
     */
    private Text lives;

    /**
     * The root Group that contains the game objects.
     */
    private Group root;

    /**
     * Constructor for Score class.
     * Initializes the score and lives text objects and adds them to the root group.
     *
     * @param root  The Group object representing the root node of the scene graph.
     */
    public Score(Group root) {
        this.root = root;
        this.score = initializeText("SCORE: 0", Obstacle.THICKNESS * 4, Obstacle.THICKNESS * 28, Color.GOLD, Font.font("Arial Black", 30));
        this.lives = initializeText("LIVES: 3", Obstacle.THICKNESS * 20, Obstacle.THICKNESS * 28, Color.GOLD, Font.font("Arial", 30));
        root.getChildren().addAll(score, lives);
    }

    /**
     * Initialize a Text object with given properties
     *
     * @param content Initial text content.
     * @param x X coordinate for the text position.
     * @param y Y coordinate for the text position.
     * @param fill Fill color for the text.
     * @param font Font for the text.
     * @return Initialized Text object.
     */
    private Text initializeText(String content, double x, double y, Color fill, Font font) {
        Text text = new Text(x, y, content);
        text.setFill(fill);
        text.setFont(font);
        return text;
    }

    /**
     * Updates the score text with the new score value.
     *
     * @param newScore The new score value.
     */
    public void updateScore(int newScore) {
        score.setText("SCORE: " + newScore);
    }

    /**
     * Updates the lives text with the new lives count.
     *
     * @param newLives The new lives count.
     */
    public void updateLives(int newLives) {
        lives.setText("LIVES: " + newLives);
    }

    /**
     * Removes score and lives texts from the root group.
     * Useful for clearing the screen or resetting the score and lives.
     */
    public void removeTextsFromRoot() {
        root.getChildren().removeAll(score, lives);
    }

    /**
     * Adds score and lives texts to the root group.
     * Useful for displaying the score and lives after being removed or at the start of the game.
     */
    public void addTextsToRoot() {
        root.getChildren().addAll(score, lives);
    }
}
