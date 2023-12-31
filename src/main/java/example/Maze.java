package example;

import javafx.scene.Group;
import java.util.HashSet;
import java.util.Set;

/**
 * The Maze class represents the layout of the maze in the Pacman game.
 * It stores a set of Obstacle objects which make up the structure of the maze.
 */
public class Maze {
    // Set of Obstacle objects that make up the maze.
    private Set<Obstacle> obstacles;

    // Manager object for game management functionalities like difficulty level.
    private Manager manager;

    /**
     * Constructor for the Maze class. Initializes an empty set of Obstacle objects.
     * @param manager Manager object for game management.
     */
    public Maze(Manager manager) {
        obstacles = new HashSet<>();
        this.manager = manager;
    }

    /**
     * Checks if a point defined by the x and y coordinates is touching an Obstacle,
     * given a certain collision buffer.
     *
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * @param collisionBuffer the collision buffer around the obstacle.
     * @return true if the point is touching an obstacle, false otherwise.
     */
    public boolean isTouching(double x, double y, double collisionBuffer) {
        for (Obstacle obstacle : obstacles) {
            // Check collision based on buffer and obstacle's position and dimensions.
            if (x >= obstacle.getX() - collisionBuffer &&
                    x <= obstacle.getX() + collisionBuffer + obstacle.getWidth() &&
                    y >= obstacle.getY() - collisionBuffer &&
                    y <= obstacle.getY() + collisionBuffer + obstacle.getHeight()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is an obstacle within a specified rectangle in the maze.
     *
     * @param fromX the starting x coordinate of the rectangle.
     * @param toX the ending x coordinate of the rectangle.
     * @param fromY the starting y coordinate of the rectangle.
     * @param toY the ending y coordinate of the rectangle.
     * @return true if there is an obstacle within the rectangle, false otherwise.
     */
    public boolean hasObstacle(double fromX, double toX, double fromY, double toY) {
        // Iterate through the rectangle coordinates to check for any obstacles.
        for (double i = fromX; i < toX; i++) {
            for (double j = fromY; j < toY; j++) {
                if (this.isTouching(i, j, 0)) return true;
            }
        }
        return false;
    }

    /**
     * Creates the layout of the maze and adds the Obstacle objects to the specified Group root.
     *
     * @param root the Group object where the Obstacle objects are added.
     */
    public void createMaze(Group root) {
        // Create the outer frame of the maze.
        createFrame();

        String difficulty = manager.getLevel();  // Fetch the difficulty level from manager.

        // Check for null and then create islands based on difficulty level.
        if (difficulty != null) {
            if (difficulty.equals("hard")) {
                createIslandsHard();
            } else if (difficulty.equals("normal")) {
                createIslandsNormal();
            }
        }

        // Create enclosed areas called cages.
        createCages();

        // Add all obstacles to the Group.
        root.getChildren().addAll(obstacles);
    }

    /**
     * Creates the frame of the maze by adding Obstacle objects to the edges of the maze.
     */
    private void createFrame() {
        // Create top, bottom, left, and right edges of the maze.
        obstacles.add(new Obstacle(0, 0, "horizontal", 48));
        obstacles.add(new Obstacle(0, 600, "horizontal", 48));
        obstacles.add(new Obstacle(0, 0, "vertical", 25));
        obstacles.add(new Obstacle(1225 - Obstacle.THICKNESS, 0, "vertical", 25));
    }

    /**
     * Creates "islands" or clusters of obstacles within the maze for normal difficulty.
     */
    private void createIslandsNormal() {
        // ObsTopLeft
        this.obstacles.add(new Obstacle(12 * Obstacle.THICKNESS, Obstacle.THICKNESS, "vertical", 4));
        // obsTopRight
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, Obstacle.THICKNESS, "vertical", 4));
        // obsBottomLeft
        this.obstacles.add(new Obstacle(12 * Obstacle.THICKNESS, 600 - 4 * Obstacle.THICKNESS, "vertical", 4));
        // obsBottomRight
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, 600 - 4 * Obstacle.THICKNESS, "vertical", 4));
        // obsTopMiddle
        this.obstacles.add(new Obstacle(16 * Obstacle.THICKNESS, 4 * Obstacle.THICKNESS, "horizontal", 17));
        // obsBottomMiddle
        this.obstacles.add(new Obstacle(16 * Obstacle.THICKNESS, 600 - 4 * Obstacle.THICKNESS, "horizontal", 17));
        // obsLMTop
        this.obstacles.add(new Obstacle(8 * Obstacle.THICKNESS, 8 * Obstacle.THICKNESS, "horizontal", 5));
        // obsLMTop4
        this.obstacles.add(new Obstacle(8 * Obstacle.THICKNESS, 12 * Obstacle.THICKNESS, "horizontal", 5));
        // obsLMBottom
        this.obstacles.add(new Obstacle(8 * Obstacle.THICKNESS, 16 * Obstacle.THICKNESS, "horizontal", 5));
        // obsRMTop
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, 8 * Obstacle.THICKNESS, "horizontal", 5));
        // obsRMTop2
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, 12 * Obstacle.THICKNESS, "horizontal", 5));
        // obsRMBottom
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, 16 * Obstacle.THICKNESS, "horizontal", 5));
        // LobsLeftTop1
        this.obstacles.add(new Obstacle(4 * Obstacle.THICKNESS, 4 * Obstacle.THICKNESS, "horizontal", 5));
        // LobsLeftTop2
        this.obstacles.add(new Obstacle(4 * Obstacle.THICKNESS, 5 * Obstacle.THICKNESS, "vertical", 6));
        // LobsLeftBottom1
        this.obstacles.add(new Obstacle(4 * Obstacle.THICKNESS, 600 - 4 * Obstacle.THICKNESS, "horizontal", 5));
        // LobsLeftBottom2
        this.obstacles.add(new Obstacle(4 * Obstacle.THICKNESS, 600 - 10 * Obstacle.THICKNESS, "vertical", 6));
        // LobsRightTop1
        this.obstacles.add(new Obstacle(40 * Obstacle.THICKNESS, 4 * Obstacle.THICKNESS, "horizontal", 5));
        // LobsRightTop2
        this.obstacles.add(new Obstacle(44 * Obstacle.THICKNESS, 5 * Obstacle.THICKNESS, "vertical", 6));
        // LobsRightBottom1
        this.obstacles.add(new Obstacle(40 * Obstacle.THICKNESS, 600 - 4 * Obstacle.THICKNESS, "horizontal", 5));
        // LobsRightBottom2
        this.obstacles.add(new Obstacle(44 * Obstacle.THICKNESS, 600 - 10 * Obstacle.THICKNESS, "vertical", 6));
    }

    /**
     * Creates "cages" or enclosed areas within the maze.
     */
    private void createCages() {
        // cageBottom
        this.obstacles.add(new Obstacle(16 * Obstacle.THICKNESS, 600 - 8 * Obstacle.THICKNESS, "horizontal", 17));
        // cageRightV
        this.obstacles.add(new Obstacle(32 * Obstacle.THICKNESS, 600 - 16 * Obstacle.THICKNESS, "vertical", 8));
        // cageLeftV
        this.obstacles.add(new Obstacle(16 * Obstacle.THICKNESS, 600 - 16 * Obstacle.THICKNESS, "vertical", 8));
        // cateRightH
        this.obstacles.add(new Obstacle(17 * Obstacle.THICKNESS, 8 * Obstacle.THICKNESS, "horizontal", 5));
        // cateLeftH
        this.obstacles.add(new Obstacle(27 * Obstacle.THICKNESS, 8 * Obstacle.THICKNESS, "horizontal", 5));
    }

    /**
     * Creates "islands" or clusters of obstacles within the maze for hard difficulty.
     */
    private void createIslandsHard() {
        // ObsTopLeft
        this.obstacles.add(new Obstacle(12 * Obstacle.THICKNESS, Obstacle.THICKNESS, "vertical", 7)); //TICK
        // obsTopRight
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, Obstacle.THICKNESS, "vertical", 7)); //TICK
        // obsBottomLeft
        this.obstacles.add(new Obstacle(12 * Obstacle.THICKNESS, 600 - 8 * Obstacle.THICKNESS, "vertical", 8));
        // obsBottomRight
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, 600 - 8 * Obstacle.THICKNESS, "vertical", 8));
        // obsTopMiddle
        this.obstacles.add(new Obstacle(16 * Obstacle.THICKNESS, 4 * Obstacle.THICKNESS, "horizontal", 17)); //TICK
        // obsBottomMiddle
        this.obstacles.add(new Obstacle(16 * Obstacle.THICKNESS, 600 - 4 * Obstacle.THICKNESS, "horizontal", 17)); //TICK
        // obsLMTop
        this.obstacles.add(new Obstacle(8 * Obstacle.THICKNESS, 8 * Obstacle.THICKNESS, "horizontal", 5));
        // obsLMTop4
        this.obstacles.add(new Obstacle(8 * Obstacle.THICKNESS, 12 * Obstacle.THICKNESS, "horizontal", 5));
        // obsLMBottom
        this.obstacles.add(new Obstacle(8 * Obstacle.THICKNESS, 16 * Obstacle.THICKNESS, "horizontal", 5));
        // obsRMTop
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, 8 * Obstacle.THICKNESS, "horizontal", 5));
        // obsRMTop2
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, 12 * Obstacle.THICKNESS, "horizontal", 5));
        // obsRMBottom
        this.obstacles.add(new Obstacle(36 * Obstacle.THICKNESS, 16 * Obstacle.THICKNESS, "horizontal", 5));
        // LobsLeftTop1
        this.obstacles.add(new Obstacle(4 * Obstacle.THICKNESS, 4 * Obstacle.THICKNESS, "horizontal", 5));
        // LobsLeftTop2
        this.obstacles.add(new Obstacle(4 * Obstacle.THICKNESS, 5 * Obstacle.THICKNESS, "vertical", 15));
        // LobsLeftBottom1
        this.obstacles.add(new Obstacle(4 * Obstacle.THICKNESS, 600 - 4 * Obstacle.THICKNESS, "horizontal", 5));
        // LobsRightTop1
        this.obstacles.add(new Obstacle(40 * Obstacle.THICKNESS, 4 * Obstacle.THICKNESS, "horizontal", 5)); //TICK
        // LobsRightTop2
        this.obstacles.add(new Obstacle(44 * Obstacle.THICKNESS, 5 * Obstacle.THICKNESS, "vertical", 15));
        // LobsRightBottom1
        this.obstacles.add(new Obstacle(40 * Obstacle.THICKNESS, 600 - 4 * Obstacle.THICKNESS, "horizontal", 5)); //TICK
    }
}