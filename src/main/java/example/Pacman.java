package example;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Pacman extends Circle {
    private Manager manager;
    private AnimationTimer rightPacmanAnimation;
    private AnimationTimer leftPacmanAnimation;
    private AnimationTimer upPacmanAnimation;
    private AnimationTimer downPacmanAnimation;
    private Maze maze;
    private Set<Ghost> ghosts;

    private Group root;

    public Pacman(double x, double y, Maze maze, Manager manager, Set<Ghost> ghosts, Group root) {
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(30);
        this.manager = manager;
        this.ghosts = ghosts;
        this.root = root;

        this.setFill(Color.GOLD);

        this.maze = maze;

        // Initialize animation timers
        this.leftPacmanAnimation = this.createAnimation("left");
        this.rightPacmanAnimation = this.createAnimation("right");
        this.upPacmanAnimation = this.createAnimation("up");
        this.downPacmanAnimation = this.createAnimation("down");
    }

    public void move(KeyEvent event) {
        switch (event.getCode()) {
            case RIGHT:
                this.rightPacmanAnimation.start();
                break;
            case LEFT:
                this.leftPacmanAnimation.start();
                break;
            case UP:
                this.upPacmanAnimation.start();
                break;
            case DOWN:
                this.downPacmanAnimation.start();
                break;
        }
    }

    public void stopAllAnimations() {
        leftPacmanAnimation.stop();
        rightPacmanAnimation.stop();
        upPacmanAnimation.stop();
        downPacmanAnimation.stop();
    }

    public void stop(KeyEvent event) {
        switch (event.getCode()) {
            case RIGHT:
                this.rightPacmanAnimation.stop();
                break;
            case LEFT:
                this.leftPacmanAnimation.stop();
                break;
            case UP:
                this.upPacmanAnimation.stop();
                break;
            case DOWN:
                this.downPacmanAnimation.stop();
                break;
        }
    }

    private AnimationTimer createAnimation(String direction) {
        double step = 5;
        double collisionBuffer = 5;
        return new AnimationTimer() {
            public void handle(long currentNanoTime) {
                switch (direction) {
                    case "left":
                        if (!maze.isTouching(getCenterX() - getRadius(), getCenterY(), collisionBuffer)) {
                            setCenterX(getCenterX() - step);
                            checkCookieCollision(Pacman.this, "x");
                            checkGhostCollision();
                        }
                        break;
                    case "right":
                        if (!maze.isTouching(getCenterX() + getRadius(), getCenterY(), collisionBuffer)) {
                            setCenterX(getCenterX() + step);
                            checkCookieCollision(Pacman.this, "x");
                            checkGhostCollision();
                        }
                        break;
                    case "up":
                        if (!maze.isTouching(getCenterX(), getCenterY() - getRadius(), collisionBuffer)) {
                            setCenterY(getCenterY() - step);
                            checkCookieCollision(Pacman.this, "y");
                            checkGhostCollision();
                        }
                        break;
                    case "down":
                        if (!maze.isTouching(getCenterX(), getCenterY() + getRadius(), collisionBuffer)) {
                            setCenterY(getCenterY() + step);
                            checkCookieCollision(Pacman.this, "y");
                            checkGhostCollision();
                        }
                        break;
                }
            }
        };
    }

    private void checkCookieCollision(Pacman pacman, String axis) {
        if (manager.gameEnded) {
            return; // Don't proceed if the game is already over
        }

        double pacmanCenterY = pacman.getCenterY();
        double pacmanCenterX = pacman.getCenterX();
        double pacmanLeftEdge = pacmanCenterX - pacman.getRadius();
        double pacmanRightEdge = pacmanCenterX + pacman.getRadius();
        double pacmanTopEdge = pacmanCenterY - pacman.getRadius();
        double pacmanBottomEdge = pacmanCenterY + pacman.getRadius();

        Iterator<Cookie> iterator = manager.cookieSet.iterator();
        while (iterator.hasNext()) {
            Cookie cookie = iterator.next();
            double cookieCenterX = cookie.getCenterX();
            double cookieCenterY = cookie.getCenterY();
            double cookieLeftEdge = cookieCenterX - cookie.getRadius();
            double cookieRightEdge = cookieCenterX + cookie.getRadius();
            double cookieTopEdge = cookieCenterY - cookie.getRadius();
            double cookieBottomEdge = cookieCenterY + cookie.getRadius();

            if (axis.equals("x")) {
                // pacman goes right
                if ((cookieCenterY >= pacmanTopEdge && cookieCenterY <= pacmanBottomEdge) && (pacmanRightEdge >= cookieLeftEdge && pacmanRightEdge <= cookieRightEdge)) {
                    if (cookie.isVisible()) {
                        manager.score += cookie.getValue();
                        manager.cookiesEaten++;
                    }
                    iterator.remove();
                    root.getChildren().remove(cookie);
                }
                // pacman goes left
                if ((cookieCenterY >= pacmanTopEdge && cookieCenterY <= pacmanBottomEdge) && (pacmanLeftEdge >= cookieLeftEdge && pacmanLeftEdge <= cookieRightEdge)) {
                    if (cookie.isVisible()) {
                        manager.score += cookie.getValue();
                        manager.cookiesEaten++;
                    }
                    iterator.remove();
                    root.getChildren().remove(cookie);
                }
            } else {
                // pacman goes up
                if ((cookieCenterX >= pacmanLeftEdge && cookieCenterX <= pacmanRightEdge) && (pacmanBottomEdge >= cookieTopEdge && pacmanBottomEdge <= cookieBottomEdge)) {
                    if (cookie.isVisible()) {
                        manager.score += cookie.getValue();
                        manager.cookiesEaten++;
                    }
                    iterator.remove();
                    root.getChildren().remove(cookie);
                }
                // pacman goes down
                if ((cookieCenterX >= pacmanLeftEdge && cookieCenterX <= pacmanRightEdge) && (pacmanTopEdge <= cookieBottomEdge && pacmanTopEdge >= cookieTopEdge)) {
                    if (cookie.isVisible()) {
                        manager.score += cookie.getValue();
                        manager.cookiesEaten++;
                    }
                    iterator.remove();
                    root.getChildren().remove(cookie);
                }
            }
            manager.scoreBoard.updateScore(manager.score);  // moved outside if-else blocks to ensure it's updated
        }

        if (140 == manager.cookiesEaten && !manager.gameEnded) {
            manager.nextRound();
        }
    }

    public void checkGhostCollision() {
        boolean powerUpActive = manager.isPowerUpActive(); // Assume Manager has a method to indicate power-up state
        double pacmanCenterY = this.getCenterY();
        double pacmanCenterX = this.getCenterX();
        double pacmanLeftEdge = pacmanCenterX - this.getRadius();
        double pacmanRightEdge = pacmanCenterX + this.getRadius();
        double pacmanTopEdge = pacmanCenterY - this.getRadius();
        double pacmanBottomEdge = pacmanCenterY + this.getRadius();

        for (Ghost ghost : ghosts) {
            // Skip this iteration if the ghost is invisible
            if (!ghost.isVisible()) {
                continue;
            }

            double ghostLeftEdge = ghost.getX();
            double ghostRightEdge = ghost.getX() + ghost.getWidth();
            double ghostTopEdge = ghost.getY();
            double ghostBottomEdge = ghost.getY() + ghost.getHeight();

            if ((pacmanLeftEdge <= ghostRightEdge && pacmanLeftEdge >= ghostLeftEdge) ||
                    (pacmanRightEdge >= ghostLeftEdge && pacmanRightEdge <= ghostRightEdge)) {

                if ((pacmanTopEdge <= ghostBottomEdge && pacmanTopEdge >= ghostTopEdge) ||
                        (pacmanBottomEdge >= ghostTopEdge && pacmanBottomEdge <= ghostBottomEdge)) {

                    if (powerUpActive) {
                        ghost.setVisible(false);  // Set the ghost to be invisible
                        manager.score += 100;  // Award points for eating the ghost

                        // After 10 seconds, make the ghost reappear in the cage
                        PauseTransition pause = new PauseTransition(Duration.seconds(10));
                        pause.setOnFinished(event -> {
                            ghost.setX(ghost.getInitialX());  // Set to initial X position
                            ghost.setY(ghost.getInitialY());  // Set to initial Y position
                            ghost.setVisible(true);  // Make the ghost visible again
                        });
                        pause.play();
                    } else {
                        manager.lifeGone();  // Decrease a life or game over
                    }
                }
            }
        }
    }
}