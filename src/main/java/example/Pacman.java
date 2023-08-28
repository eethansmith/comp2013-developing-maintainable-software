package example;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Iterator;
import java.util.Set;

/**
 * The Pacman class represents the main character in the Pacman game.
 * It extends the Circle class from JavaFX to handle graphical representation.
 */
public class Pacman extends Circle {

    // Manager to keep track of the game state
    private Manager manager;

    // Animation timers for each of the directions
    private AnimationTimer rightPacmanAnimation;
    private AnimationTimer leftPacmanAnimation;
    private AnimationTimer upPacmanAnimation;
    private AnimationTimer downPacmanAnimation;

    // Reference to the Maze object to check for wall collisions
    private Maze maze;

    // Set of Ghost objects to check for collisions with
    private Set<Ghost> ghosts;

    // Group for adding/removing objects from the scene
    private Group root;

    /**
     * Constructor for the Pacman object.
     * @param x The initial X coordinate
     * @param y The initial Y coordinate
     * @param maze The Maze object
     * @param manager The Manager object to manage game state
     * @param ghosts Set of Ghost objects in the game
     * @param root The root Group for adding/removing JavaFX nodes
     */
    public Pacman(double x, double y, Maze maze, Manager manager, Set<Ghost> ghosts, Group root) {
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(30);  // Setting the radius of Pacman
        this.manager = manager;
        this.ghosts = ghosts;
        this.root = root;

        // Set the color of the Pacman
        this.setFill(Color.GOLD);

        this.maze = maze;

        // Initialize animation timers for each direction
        this.leftPacmanAnimation = this.createAnimation("left");
        this.rightPacmanAnimation = this.createAnimation("right");
        this.upPacmanAnimation = this.createAnimation("up");
        this.downPacmanAnimation = this.createAnimation("down");
    }

    /**
     * Method to move Pacman based on the key event.
     * @param event KeyEvent indicating which key was pressed
     */
    public void move(KeyEvent event) {
        switch (event.getCode()) {
            case RIGHT:
                // Start the animation to move Pacman to the right
                this.rightPacmanAnimation.start();
                break;
            case LEFT:
                // Start the animation to move Pacman to the left
                this.leftPacmanAnimation.start();
                break;
            case UP:
                // Start the animation to move Pacman upwards
                this.upPacmanAnimation.start();
                break;
            case DOWN:
                // Start the animation to move Pacman downwards
                this.downPacmanAnimation.start();
                break;
        }
    }

    /**
     * Stops all animations for Pacman.
     */
    public void stopAllAnimations() {
        leftPacmanAnimation.stop();
        rightPacmanAnimation.stop();
        upPacmanAnimation.stop();
        downPacmanAnimation.stop();
    }

    /**
     * Method to stop Pacman's movement based on the key event.
     * @param event KeyEvent indicating which key was released
     */
    public void stop(KeyEvent event) {
        switch (event.getCode()) {
            case RIGHT:
                // Stop the animation to move Pacman to the right
                this.rightPacmanAnimation.stop();
                break;
            case LEFT:
                // Stop the animation to move Pacman to the left
                this.leftPacmanAnimation.stop();
                break;
            case UP:
                // Stop the animation to move Pacman upwards
                this.upPacmanAnimation.stop();
                break;
            case DOWN:
                // Stop the animation to move Pacman downwards
                this.downPacmanAnimation.stop();
                break;
        }
    }


    /**
     * Creates an animation for moving the Pacman in the specified direction.
     * @param direction The direction to move the Pacman. Can be "left", "right", "up", "down".
     * @return AnimationTimer for controlling the animation.
     */
    private AnimationTimer createAnimation(String direction) {
        double step = 5;  // The distance Pacman moves in each frame
        double collisionBuffer = 5;  // Buffer to avoid collisions with walls

        // Create and return an animation timer
        return new AnimationTimer() {
            /**
             * Handles each animation frame.
             * @param currentNanoTime The current time in nanoseconds.
             */
            public void handle(long currentNanoTime) {
                switch (direction) {
                    case "left":
                        // Check if Pacman would collide with the wall
                        if (!maze.isTouching(getCenterX() - getRadius(), getCenterY(), collisionBuffer)) {
                            // Move Pacman left
                            setCenterX(getCenterX() - step);
                            // Check for collision with cookies and ghosts
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

    /**
     * Checks if Pacman collides with any cookies.
     * @param pacman The Pacman instance being checked.
     * @param axis The axis ("x" or "y") along which Pacman is moving.
     */
    private void checkCookieCollision(Pacman pacman, String axis) {
        // Early exit if the game is already over
        if (manager.gameEnded) {
            return;
        }

        // Calculate edges for collision detection
        double pacmanCenterY = pacman.getCenterY();
        double pacmanCenterX = pacman.getCenterX();
        double pacmanLeftEdge = pacmanCenterX - pacman.getRadius();
        double pacmanRightEdge = pacmanCenterX + pacman.getRadius();
        double pacmanTopEdge = pacmanCenterY - pacman.getRadius();
        double pacmanBottomEdge = pacmanCenterY + pacman.getRadius();

        // Loop through all cookies to check for collisions
        Iterator<Cookie> iterator = manager.cookieSet.iterator();
        while (iterator.hasNext()) {
            Cookie cookie = iterator.next();
            // Calculate cookie edges for collision detection
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
            // Update the score display
            manager.scoreBoard.updateScore(manager.score);
        }

        // Check if we need to proceed to the next round
        if (140 == manager.cookiesEaten && !manager.gameEnded) {
            manager.nextRound();
        }
    }

    /**
     * Checks if Pacman collides with any ghosts and handles the collision appropriately.
     */
    public void checkGhostCollision() {
        // Check if power-up is active
        boolean powerUpActive = manager.isPowerUpActive(); // Assume Manager has a method to indicate power-up state

        // Calculate Pacman edges for collision detection
        double pacmanCenterY = this.getCenterY();
        double pacmanCenterX = this.getCenterX();
        double pacmanLeftEdge = pacmanCenterX - this.getRadius();
        double pacmanRightEdge = pacmanCenterX + this.getRadius();
        double pacmanTopEdge = pacmanCenterY - this.getRadius();
        double pacmanBottomEdge = pacmanCenterY + this.getRadius();

        // Loop through all ghosts to check for collisions
        for (Ghost ghost : ghosts) {
            // Skip this iteration if the ghost is invisible
            if (!ghost.isVisible()) {
                continue;
            }

            // Calculate ghost edges for collision detection
            double ghostLeftEdge = ghost.getX();
            double ghostRightEdge = ghost.getX() + ghost.getWidth();
            double ghostTopEdge = ghost.getY();
            double ghostBottomEdge = ghost.getY() + ghost.getHeight();

            // Check for collisions along the x-axis
            if ((pacmanLeftEdge <= ghostRightEdge && pacmanLeftEdge >= ghostLeftEdge) ||
                    (pacmanRightEdge >= ghostLeftEdge && pacmanRightEdge <= ghostRightEdge)) {
                // Check for collisions along the y-axis
                if ((pacmanTopEdge <= ghostBottomEdge && pacmanTopEdge >= ghostTopEdge) ||
                        (pacmanBottomEdge >= ghostTopEdge && pacmanBottomEdge <= ghostBottomEdge)) {
                    // Handle collision based on whether power-up is active
                    if (powerUpActive) {
                        ghost.setVisible(false);  // Set the ghost to be invisible
                        manager.score += 50;  // Award points for eating the ghost

                        // After 10 seconds, make the ghost reappear in the cage
                        PauseTransition pause = new PauseTransition(Duration.seconds(10));
                        pause.setOnFinished(event -> {
                            ghost.setX(ghost.getInitialX());  // Set to initial X position
                            ghost.setY(ghost.getInitialY());  // Set to initial Y position
                            ghost.setVisible(true);  // Make the ghost visible again
                        });
                        pause.play();
                    } else {
                        // If power-up is not active, decrease life or trigger game over
                        manager.lifeGone();
                    }
                }
            }
        }
    }
}
