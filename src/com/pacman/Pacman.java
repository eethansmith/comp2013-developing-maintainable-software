package example;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Set;

public class Pacman extends Circle {
    private Manager manager;
    private AnimationTimer rightPacmanAnimation;
    private AnimationTimer leftPacmanAnimation;
    private AnimationTimer upPacmanAnimation;
    private AnimationTimer downPacmanAnimation;
    private Maze maze;
    private Set<Ghost> ghosts;


    public Pacman(double x, double y, String imagePath, Maze maze, Manager manager, Set<Ghost> ghosts) {
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(25);
        this.manager = manager;
        this.ghosts = ghosts;

        // Load the image and set it as the fill pattern
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        ImagePattern imagePattern = new ImagePattern(image);
        this.setFill(imagePattern);

        this.maze = maze;

        // Initialize animation timers
        this.leftPacmanAnimation = this.createAnimation("left");
        this.rightPacmanAnimation = this.createAnimation("right");
        this.upPacmanAnimation = this.createAnimation("up");
        this.downPacmanAnimation = this.createAnimation("down");
    }

    public void move(KeyEvent event) {
        switch(event.getCode()) {
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
        switch(event.getCode()) {
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
        for (Cookie cookie : manager.cookieSet) {
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
                    cookie.hide();
                }
                // pacman goes left
                if ((cookieCenterY >= pacmanTopEdge && cookieCenterY <= pacmanBottomEdge) && (pacmanLeftEdge >= cookieLeftEdge && pacmanLeftEdge <= cookieRightEdge)) {
                    if (cookie.isVisible()) {
                        manager.score += cookie.getValue();
                        manager.cookiesEaten++;
                    }
                    cookie.hide();
                }
            } else {
                // pacman goes up
                if ((cookieCenterX >= pacmanLeftEdge && cookieCenterX <= pacmanRightEdge) && (pacmanBottomEdge >= cookieTopEdge && pacmanBottomEdge <= cookieBottomEdge)) {
                    if (cookie.isVisible()) {
                        manager.score += cookie.getValue();
                        manager.cookiesEaten++;
                    }
                    cookie.hide();
                }
                // pacman goes down
                if ((cookieCenterX >= pacmanLeftEdge && cookieCenterX <= pacmanRightEdge) && (pacmanTopEdge <= cookieBottomEdge && pacmanTopEdge >= cookieTopEdge)) {
                    if (cookie.isVisible()) {
                        manager.score += cookie.getValue();
                        manager.cookiesEaten++;
                    }
                    cookie.hide();
                }
            }
            manager.scoreBoard.updateScore(manager.score);
            if (manager.cookiesEaten == manager.cookieSet.size()) {
                if (manager.cookiesEaten == manager.cookieSet.size() && !manager.gameEnded) {
                    manager.nextRound();
                }
            }
        }
    }
    public void checkGhostCollision() {
        double pacmanCenterY = this.getCenterY();
        double pacmanCenterX = this.getCenterX();
        double pacmanLeftEdge = pacmanCenterX - this.getRadius();
        double pacmanRightEdge = pacmanCenterX + this.getRadius();
        double pacmanTopEdge = pacmanCenterY - this.getRadius();
        double pacmanBottomEdge = pacmanCenterY + this.getRadius();
        this.ghosts = manager.getGhosts();
        for (Ghost ghost : this.ghosts) {
            double ghostLeftEdge = ghost.getX();
            double ghostRightEdge = ghost.getX() + ghost.getWidth();
            double ghostTopEdge = ghost.getY();
            double ghostBottomEdge = ghost.getY() + ghost.getHeight();
            if ((pacmanLeftEdge <= ghostRightEdge && pacmanLeftEdge >= ghostLeftEdge) || (pacmanRightEdge >= ghostLeftEdge && pacmanRightEdge <= ghostRightEdge)) {
                if ((pacmanTopEdge <= ghostBottomEdge && pacmanTopEdge >= ghostTopEdge) || (pacmanBottomEdge >= ghostTopEdge && pacmanBottomEdge <= ghostBottomEdge)) {
                    manager.lifeGone();
                }
            }
        }
    }
}