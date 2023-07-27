package sample;



import javafx.scene.Group;

import java.util.HashSet;
import java.util.Set;

public class Maze {

    public Set<Obstacle> obstacles;

    Maze() {
        obstacles = new HashSet<>();
    }

    public Boolean isTouching(double x, double y, double padding) {
        for (Obstacle obstacle :obstacles) {
            if (
                x >= obstacle.getX() - padding &&
                x <= obstacle.getX() + padding + obstacle.getWidth() &&
                y >= obstacle.getY() - padding &&
                y <= obstacle.getY() + padding + obstacle.getHeight())
            {
                return true;
            }
        }
        return false;
    }

    public Boolean hasObstacle(double fromX,  double toX, double fromY, double toY) {
        boolean isTouching = false;
        for (double i = fromX; i < toX; i++) {
            for (double j = fromY; j < toY; j++) {
                if (this.isTouching(i, j, 0)) isTouching = true;
            }
        }
        return isTouching;
    }

    public void CreateMaze(Group root) {
        //~~~~~~~~~~~~~~~~~~~~~~~~~ frame ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // top
        this.obstacles.add(new Obstacle(0, 0, "horizontal", 48));
        // bottom
        this.obstacles.add(new Obstacle(0, 600, "horizontal", 48));
        // left
        this.obstacles.add(new Obstacle(0, 0, "vertical", 25));
        // right
        this.obstacles.add(new Obstacle(1225 - Obstacle.THICKNESS, 0, "vertical", 25));

        //~~~~~~~~~~~~~~~~~~~~~~~~~ Islands ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // obsTopLeft
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

        root.getChildren().addAll(obstacles);
    }
}
