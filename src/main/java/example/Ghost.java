package example;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import java.util.Random;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;

/**
 * example.Ghost class represents a ghost character in a example.Pacman game.
 * example.Ghost moves autonomously within the maze and interacts with the player.
 */
public class Ghost extends Rectangle implements Runnable {

    /**
     * Direction in which the example.Ghost is currently moving.
     */
    String direction;

    /**
     * Reference to the example.Manager class to check for collisions.
     */
    Manager manager;

    /**
     * Reference to the example.Maze in which the example.Ghost moves.
     */
    Maze maze;

    /**
     * AnimationTimer object controlling the example.Ghost's movement.
     */
    AnimationTimer animation;

    /**
     * Counter for the number of steps the example.Ghost has taken.
     */
    int timesWalked;

    private Scale scale;

    private Image normalImage;
    private Image flippedImage;
    private ImagePattern imagePattern;
    private Pacman pacman;

    private Image blueImage;
    private boolean powerUpActive;
    private double initialX;
    private double initialY;

    /**
     * Ghost constructor. Initializes a Ghost instance with given parameters.
     *
     * @param x x-coordinate of the Ghost's position
     * @param y y-coordinate of the Ghost's position
     * @param imagePath Path of the image to be used for the Ghost
     * @param maze Reference to the Maze
     * @param manager Reference to the Manager
     */
    public Ghost(double x, double y, String imagePath, Maze maze, Manager manager, Pacman pacman) {
        super(x, y);
        // Set the x-coordinate of the example.Ghost
        this.setX(x);
        // Set the y-coordinate of the example.Ghost
        this.setY(y);
        // Assign the example.Maze object
        this.maze = maze;
        // Assign the example.Manager object
        this.manager = manager;
        // Initialize the Scale object
        this.scale = new Scale(1, 1);
        // Add the scale transform to the Ghost
        this.getTransforms().add(scale);
        // Set the height of the example.Ghost
        this.setHeight(50);
        // Set the width of the example.Ghost
        this.setWidth(50);
        this.normalImage = new Image(getClass().getResource(imagePath).toExternalForm());
        this.setFill(imagePattern);
        // Initialize the step counter to 0
        this.timesWalked = 0;
        // Set initial moving direction to down
        this.direction = "down";
        this.pacman = pacman;
        // Create the movement animation for the example.Ghost
        this.createAnimation();
        this.imagePattern = new ImagePattern(this.normalImage);  // Initialize imagePattern
        this.setFill(this.imagePattern);
        this.blueImage = new Image(getClass().getResource("Ghost-Blue.png").toExternalForm());
        boolean powerUpActive;

        this.initialX = x;
        this.initialY = y;


// Create the flipped image
        PixelReader reader = normalImage.getPixelReader();
        WritableImage newImage = new WritableImage((int) normalImage.getWidth(), (int) normalImage.getHeight());
        PixelWriter writer = newImage.getPixelWriter();
        for (int pixelY = 0; pixelY < normalImage.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < normalImage.getWidth(); pixelX++) {
                writer.setColor((int) (normalImage.getWidth() - pixelX - 1), pixelY, reader.getColor(pixelX, pixelY));
            }
        }
        this.flippedImage = newImage;
        // Set the initial image to the normal image.
        this.setFill(new ImagePattern(normalImage));
    }

    public void setPowerUpActive(boolean isActive) {
        this.powerUpActive = isActive;
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    /**
     * Generates a random direction excluding the two provided directions.
     *
     * @param exclude1 Direction to exclude
     * @param exclude2 Another direction to exclude
     * @return String Randomly selected direction
     */
    private String getRandomDirection(String exclude1, String exclude2) {
        // Possible directions
        String[] directions = {"left", "right", "up", "down"};
        // Generate a random index for direction array
        int rnd = new Random().nextInt(directions.length);
        // Keep generating a new index until a direction other than exclude1 and exclude2 is selected
        while (directions[rnd].equals(exclude1) || directions[rnd].equals(exclude2)) {
            rnd = new Random().nextInt(directions.length);
        }
        // Return the selected direction
        return directions[rnd];
    }

    /**
     * Generates a random boolean value.
     *
     * @return boolean Randomly generated true or false.
     */
    private boolean getRandomBoolean() {
        // Create Random object
        Random rand = new Random();
        // Return a random boolean value
        return rand.nextBoolean();
    }

    /**
     * Returns the AnimationTimer instance that controls the example.Ghost's movement.
     *
     * @return AnimationTimer The animation timer of the example.Ghost
     */
    public AnimationTimer getAnimation() {
        // Return the animation timer controlling the example.Ghost's movement
        return animation;
    }

    /**
     * Checks if there's a path available in the provided direction.
     *
     * @param direction Direction to check for an available path
     */
    private void checkIfTheresPathToGo(String direction) {
        // Variables for each edge of the example.Ghost
        double rightEdge, leftEdge, topEdge, bottomEdge;
        // Use a switch to check if there's a path in the direction provided
        switch (direction) {
            case "down":
                // Calculate each edge based on the example.Ghost's current position and size
                leftEdge = getX() - 10;
                bottomEdge = getY() + getHeight() + 15;
                rightEdge = getX() + getWidth() + 10;
                // If there's no obstacle, set the example.Ghost's direction to down
                if (!maze.hasObstacle(leftEdge, rightEdge, bottomEdge - 1, bottomEdge)) {
                    this.direction = direction;
                }
                break;
            case "up":
                // Calculating each edge
                leftEdge = getX() - 10;
                rightEdge = getX() + getWidth() + 10;
                topEdge = getY() - 15;
                // If there's no obstacle, set the example.Ghost's direction to up
                if (!maze.hasObstacle(leftEdge, rightEdge, topEdge - 1, topEdge)) {
                    this.direction = direction;
                }
                break;
            case "left":
                // Calculating each edge
                leftEdge = getX() - 15;
                bottomEdge = getY() + getHeight() + 10;
                topEdge = getY() - 10;
                // If there's no obstacle, set the example.Ghost's direction to left
                if (!maze.hasObstacle(leftEdge - 1, leftEdge, topEdge, bottomEdge)) {
                    this.direction = direction;
                }
                break;
            case "right":
                // Calculating each edge
                bottomEdge = getY() + getHeight() + 10;
                rightEdge = getX() + getWidth() + 15;
                topEdge = getY() - 10;
                // If there's no obstacle, set the example.Ghost's direction to right
                if (!maze.hasObstacle(rightEdge - 1, rightEdge, topEdge, bottomEdge)) {
                    this.direction = direction;
                }
                break;
        }
    }

    /**
     * Moves the example.Ghost in the provided direction until it can't move further.
     *
     * @param whereToGo Direction in which to move
     * @param whereToChangeTo Direction to switch to upon obstruction
     * @param leftEdge Left edge coordinate
     * @param topEdge Top edge coordinate
     * @param rightEdge Right edge coordinate
     * @param bottomEdge Bottom edge coordinate
     * @param padding Padding to consider while moving
     */
    private void moveUntilYouCant(String whereToGo, String whereToChangeTo, double leftEdge, double topEdge, double rightEdge, double bottomEdge, double padding) {
        double step = 5;
        switch (whereToGo) {
            case "left":
                if (!maze.isTouching(leftEdge, topEdge, padding)) {
                    setX(leftEdge - step);
                } else {
                    while (maze.isTouching(getX(), getY(), padding)) {
                        setX(getX() + 1);
                    }
                    direction = whereToChangeTo;
                }
                break;
            case "right":
                if (!maze.isTouching(rightEdge, topEdge, padding)) {
                    setX(leftEdge + step);
                } else {
                    while (maze.isTouching(getX() + getWidth(), getY(), padding)) {
                        setX(getX() - 1);
                    }
                    direction = whereToChangeTo;
                }
                break;
            case "up":
                if (!maze.isTouching(leftEdge, topEdge, padding)) {
                    setY(topEdge - step);
                } else {
                    while (maze.isTouching(getX(), getY(), padding)) {
                        setY(getY() + 1);
                    }
                    direction = "left";
                }
                break;
            case "down":
                if (!maze.isTouching(leftEdge, bottomEdge, padding)) {
                    setY(topEdge + step);
                } else {
                    while (maze.isTouching(getX(), getY() + getHeight(), padding)) {
                        setY(getY() - 1);
                    }
                    direction = "right";
                }
                break;
        }

    }

    /**
     * Creates an animation for the example.Ghost's movement.
     */
    public void createAnimation() {
        this.animation = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (pacman != null) {  // Check if pacman is not null
                    pacman.checkGhostCollision();
                }
                if (powerUpActive) {
                    setFill(new ImagePattern(blueImage));
                } else {
                    setFill(direction.equals("left") ? new ImagePattern(flippedImage) : new ImagePattern(normalImage));
                }
                double leftEdge = getX();
                double topEdge = getY();
                double rightEdge = getX() + getWidth();
                double bottomEdge = getY() + getHeight();
                double padding = 12;
                timesWalked++;
                int walkAtLeast = 4;
                switch (direction) {
                    case "left":
                        moveUntilYouCant("left", "down", leftEdge, topEdge, rightEdge, bottomEdge, padding);
                        setFill(new ImagePattern(flippedImage));
                        if (timesWalked > walkAtLeast) {
                            checkIfTheresPathToGo(getRandomDirection("left", "right"));
                            timesWalked = 0;
                        }
                        break;
                    case "right":
                        moveUntilYouCant("right", "up", leftEdge, topEdge, rightEdge, bottomEdge, padding);
                        setFill(new ImagePattern(normalImage));
                        if (timesWalked > walkAtLeast) {
                            checkIfTheresPathToGo(getRandomDirection("left", "right"));
                            timesWalked = 0;
                        }
                        break;
                    case "up":
                        moveUntilYouCant("up", "left", leftEdge, topEdge, rightEdge, bottomEdge, padding);
                        if (timesWalked > walkAtLeast) {
                            checkIfTheresPathToGo(getRandomDirection("up", "down"));
                            timesWalked = 0;
                        }
                        break;
                    case "down":
                        moveUntilYouCant("down", "right", leftEdge, topEdge, rightEdge, bottomEdge, padding);
                        if (timesWalked > walkAtLeast) {
                            checkIfTheresPathToGo(getRandomDirection("up", "down"));
                            timesWalked = 0;
                        }
                        break;
                }
            }
        };
    }
    public double getInitialX() {
        return initialX;
    }

    // Setter for initialX (optional)
    public void setInitialX(double initialX) {
        this.initialX = initialX;
    }

    // Getter for initialY
    public double getInitialY() {
        return initialY;
    }

    // Setter for initialY (optional)
    public void setInitialY(double initialY) {
        this.initialY = initialY;
    }

    /**
     * Starts the animation timer for the example.Ghost's movement.
     */
    @Override
    public void run() {
        this.animation.start();
    }
}
