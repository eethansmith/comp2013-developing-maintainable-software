package com.pacman;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Score {

    public Text score;
    public Text lives;

    Score(Group root) {
        this.score = new Text(Obstacle.THICKNESS * 4, Obstacle.THICKNESS * 28, "Score: 0");
        this.lives = new Text(Obstacle.THICKNESS * 20, Obstacle.THICKNESS * 28,"Lives: 3");
        score.setFill(Color.MAGENTA);
        score.setFont(Font.font("Arial", 30));

        lives.setFill(Color.MAROON);
        lives.setFont(Font.font("Arial", 30));

        root.getChildren().add(score);
        root.getChildren().add(lives);
    }
}
