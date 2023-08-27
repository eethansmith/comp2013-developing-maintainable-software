package example;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Manager {

    private Pacman pacman;
    private Group root;
    private Set<Cookie> cookieSet;
    private Set<Ghost> ghosts;
    private AnimationTimer leftPacmanAnimation;
    private AnimationTimer rightPacmanAnimation;
    private AnimationTimer upPacmanAnimation;
    private AnimationTimer downPacmanAnimation;
    private Maze maze;
    private int lives;
    private int score;
    private GameMetrics scoreBoard;
    private boolean gameEnded;
    private int cookiesEaten;
    private static String name;
    private Stage gameStage;

    Manager(Group root, Stage gameStage) {
        this.root = root;
        this.gameStage = gameStage;
        this.maze = new Maze();
        this.pacman = new Pacman(2.5 * Obstacle.THICKNESS, 2.5 * Obstacle.THICKNESS);
        this.cookieSet = new HashSet<>();
        this.ghosts = new HashSet<>();
        this.leftPacmanAnimation = this.createAnimation("left");
        this.rightPacmanAnimation = this.createAnimation("right");
        this.upPacmanAnimation = this.createAnimation("up");
        this.downPacmanAnimation = this.createAnimation("down");
        this.lives = 3;
        this.score = 0;
        this.cookiesEaten = 0;
    }

    private void lifeGone() {
        this.leftPacmanAnimation.stop();
        this.rightPacmanAnimation.stop();
        this.upPacmanAnimation.stop();
        this.downPacmanAnimation.stop();
        for (Ghost ghost : ghosts) {
            ghost.getAnimation().stop();
        }
        this.pacman.setCenterX(2.5 * Obstacle.THICKNESS);
        this.pacman.setCenterY(2.5 * Obstacle.THICKNESS);
        lives--;
        score -= 10;
        this.scoreBoard.updateScore(this.score);
        this.scoreBoard.updateLives(this.lives);
        if (lives == 0) {
            this.gameOver();
        }
    }
    private void gameOver() {
        gameEnded = true;
        root.getChildren().remove(pacman);
        for (Ghost ghost : ghosts) {
            root.getChildren().remove(ghost);
        }
        javafx.scene.text.Text endGame = new javafx.scene.text.Text("Game Over, press ESC to restart");
        endGame.setX(Obstacle.THICKNESS * 3);
        endGame.setY(Obstacle.THICKNESS * 28);
        endGame.setFont(Font.font("Arial Black", 40));
        endGame.setFill(Color.ROYALBLUE);
        this.scoreBoard.removeTextsFromRoot();
        root.getChildren().add(endGame);
        String playerName = Manager.getName();
        saveScoreToCSV(playerName, score);
        Platform.runLater(() -> {
            if (gameStage != null) {
                gameStage.close();
            }
            try {
                new ScoreboardScreen().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
    public void restartGame(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE && gameEnded) {
            root.getChildren().clear();
            this.cookieSet.clear();
            this.ghosts.clear();
            this.drawMaze();
            this.pacman.setCenterX(2.5 * Obstacle.THICKNESS);
            this.pacman.setCenterY(2.5 * Obstacle.THICKNESS);
            this.lives = 3;
            this.score = 0;
            this.cookiesEaten = 0;
            gameEnded = false;
        }
    }


    public void setPlayerName(String playerName) {
        // Check if playerName is empty or null, and set it to "N/A" if it is
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "N/A";  // Use 'this' keyword to refer to the member variable
        }
        System.out.println("Received player name: " + playerName);
        this.name = playerName;
    }

    public static String getName() {
        return name;
    }

    private void saveScoreToCSV(String playerName, int playerScore) {
        File csvFile = new File("player_scores.csv");
        List<String[]> records = new ArrayList<>();

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        try {
            // Check if the file exists. If not, create it and add the header
            if (!csvFile.exists()) {
                csvFile.createNewFile();
                try (FileWriter csvWriter = new FileWriter(csvFile)) {
                    csvWriter.append("PlayerName,PlayerScore,Recorded\n");
                    csvWriter.append(playerName).append(",").append(Integer.toString(playerScore)).append(",").append(timeStamp).append("\n");
                }
            } else {
                // File already exists, just append to it
                try (FileWriter csvWriter = new FileWriter(csvFile, true)) {
                    csvWriter.append(playerName).append(",").append(Integer.toString(playerScore)).append(",").append(timeStamp).append("\n");
                }
            }

            // Read all existing records
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                // Skip the header line
                br.readLine();
                while ((line = br.readLine()) != null) {
                    records.add(line.split(","));
                }
            }

            // Sort records based on scores in descending order
            records.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

            // Write the sorted list back to the file
            try (FileWriter csvWriter = new FileWriter(csvFile)) {
                csvWriter.append("PlayerName,PlayerScore\n");
                for (String[] record : records) {
                    csvWriter.append(String.join(",", record)).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void drawMaze() {
        // Create a BackgroundImage
        Image backgroundImage = new Image(getClass().getResource("/example/MainMenuBackground.jpg").toExternalForm(), 1225, 600, false, true);
        ImagePattern imagePattern = new ImagePattern(backgroundImage);

        Rectangle rect = new Rectangle(0, 0, 1225, 600);
        rect.setFill(imagePattern);

        root.getChildren().add(0, rect);

        this.maze.createMaze(root);
        // 1st line
        Integer skip[] = {5, 17};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2*i) + 2.5) * Obstacle.THICKNESS, 2.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 1
        skip = new Integer[]{1, 2, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 19, 20, 21};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2*i) + 2.5) * Obstacle.THICKNESS, 4.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 2
        skip = new Integer[]{1, 21};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2*i) + 2.5) * Obstacle.THICKNESS, 6.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 3
        skip = new Integer[]{1, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 21};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2 * i) + 2.5) * Obstacle.THICKNESS, 8.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 4
        skip = new Integer[]{1, 7, 8, 9, 10, 11, 12, 13, 14, 15, 21};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2*i) + 2.5) * Obstacle.THICKNESS, 10.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 5
        skip = new Integer[]{3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2*i) + 2.5) * Obstacle.THICKNESS, 12.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 6
        skip = new Integer[]{1, 7, 8, 9, 10, 11, 12, 13, 14, 15, 21};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2 * i) + 2.5) * Obstacle.THICKNESS, 14.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 7
        skip = new Integer[]{1, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 21};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2 * i) + 2.5) * Obstacle.THICKNESS, 16.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 8
        skip = new Integer[]{1, 21};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2 * i) + 2.5) * Obstacle.THICKNESS, 18.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 9
        skip = new Integer[]{1, 2, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 19, 20, 21};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2*i) + 2.5) * Obstacle.THICKNESS, 20.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        // line 10
        skip = new Integer[]{5, 17};
        for (int i = 0; i < 23; i++) {
            if (!Arrays.asList(skip).contains(i)) {
                Cookie cookie = new Cookie(((2 * i) + 2.5) * Obstacle.THICKNESS, 22.5 * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);
            }
        }
        root.getChildren().add(this.pacman);
        this.generateGhosts();
        root.getChildren().addAll(this.ghosts);
        this.scoreBoard = new GameMetrics(root);
    }

    public void generateGhosts() {
        this.ghosts.add(new Ghost(18.5 * Obstacle.THICKNESS, 12.5 * Obstacle.THICKNESS, Color.CADETBLUE, maze, this));
        this.ghosts.add(new Ghost(22.5 * Obstacle.THICKNESS, 12.5 * Obstacle.THICKNESS, Color.GREEN, maze, this));
        this.ghosts.add(new Ghost(28.5 * Obstacle.THICKNESS, 12.5 * Obstacle.THICKNESS, Color.HOTPINK, maze, this));
        this.ghosts.add(new Ghost(28.5 * Obstacle.THICKNESS, 9.5 * Obstacle.THICKNESS, Color.PURPLE, maze, this));
        this.ghosts.add(new Ghost(18.5 * Obstacle.THICKNESS, 9.5 * Obstacle.THICKNESS, Color.RED, maze, this));
    }

    public void movePacman(KeyEvent event) {
        for (Ghost ghost : this.ghosts) {
            ghost.run();
        }
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

    public void stopPacman(KeyEvent event) {
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
        return new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                switch (direction) {
                    case "left":
                        if (!maze.isTouching(pacman.getCenterX() - pacman.getRadius(), pacman.getCenterY(), 15)) {
                            pacman.setCenterX(pacman.getCenterX() - step);
                            checkCookieCollision(pacman, "x");
                            checkGhostCollision();
                        }
                        break;
                    case "right":
                        if (!maze.isTouching(pacman.getCenterX() + pacman.getRadius(), pacman.getCenterY(), 15)) {
                            pacman.setCenterX(pacman.getCenterX() + step);
                            checkCookieCollision(pacman, "x");
                            checkGhostCollision();
                        }
                        break;
                    case "up":
                        if (!maze.isTouching(pacman.getCenterX(), pacman.getCenterY() - pacman.getRadius(), 15)) {
                            pacman.setCenterY(pacman.getCenterY() - step);
                            checkCookieCollision(pacman, "y");
                            checkGhostCollision();
                        }
                        break;
                    case "down":
                        if (!maze.isTouching(pacman.getCenterX(), pacman.getCenterY() + pacman.getRadius(), 15)) {
                            pacman.setCenterY(pacman.getCenterY() + step);
                            checkCookieCollision(pacman, "y");
                            checkGhostCollision();
                        }
                        break;
                }
            }
        };
    }

    private void checkCookieCollision(Pacman pacman, String axis) {
        if (gameEnded) {
            return; // Don't proceed if the game is already over
        }
        double pacmanCenterY = pacman.getCenterY();
        double pacmanCenterX = pacman.getCenterX();
        double pacmanLeftEdge = pacmanCenterX - pacman.getRadius();
        double pacmanRightEdge = pacmanCenterX + pacman.getRadius();
        double pacmanTopEdge = pacmanCenterY - pacman.getRadius();
        double pacmanBottomEdge = pacmanCenterY + pacman.getRadius();
        for (Cookie cookie : cookieSet) {
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
                        this.score += cookie.getValue();
                        this.cookiesEaten++;
                    }
                    cookie.hide();
                }
                // pacman goes left
                if ((cookieCenterY >= pacmanTopEdge && cookieCenterY <= pacmanBottomEdge) && (pacmanLeftEdge >= cookieLeftEdge && pacmanLeftEdge <= cookieRightEdge)) {
                    if (cookie.isVisible()) {
                        this.score += cookie.getValue();
                        this.cookiesEaten++;
                    }
                    cookie.hide();
                }
            } else {
                // pacman goes up
                if ((cookieCenterX >= pacmanLeftEdge && cookieCenterX <= pacmanRightEdge) && (pacmanBottomEdge >= cookieTopEdge && pacmanBottomEdge <= cookieBottomEdge)) {
                    if (cookie.isVisible()) {
                        this.score += cookie.getValue();
                        this.cookiesEaten++;
                    }
                    cookie.hide();
                }
                // pacman goes down
                if ((cookieCenterX >= pacmanLeftEdge && cookieCenterX <= pacmanRightEdge) && (pacmanTopEdge <= cookieBottomEdge && pacmanTopEdge >= cookieTopEdge)) {
                    if (cookie.isVisible()) {
                        this.score += cookie.getValue();
                        this.cookiesEaten++;
                    }
                    cookie.hide();
                }
            }
            this.scoreBoard.updateScore(this.score);
            if (this.cookiesEaten == this.cookieSet.size()) {
                if (!gameEnded) {
                    this.gameOver();
                }
            }
        }
    }

    public void checkGhostCollision() {
        double pacmanCenterY = pacman.getCenterY();
        double pacmanCenterX = pacman.getCenterX();
        double pacmanLeftEdge = pacmanCenterX - pacman.getRadius();
        double pacmanRightEdge = pacmanCenterX + pacman.getRadius();
        double pacmanTopEdge = pacmanCenterY - pacman.getRadius();
        double pacmanBottomEdge = pacmanCenterY + pacman.getRadius();
        for (Ghost ghost : ghosts) {
            double ghostLeftEdge = ghost.getX();
            double ghostRightEdge = ghost.getX() + ghost.getWidth();
            double ghostTopEdge = ghost.getY();
            double ghostBottomEdge = ghost.getY() + ghost.getHeight();
            if ((pacmanLeftEdge <= ghostRightEdge && pacmanLeftEdge >= ghostLeftEdge) || (pacmanRightEdge >= ghostLeftEdge && pacmanRightEdge <= ghostRightEdge)) {
                if ((pacmanTopEdge <= ghostBottomEdge && pacmanTopEdge >= ghostTopEdge) || (pacmanBottomEdge >= ghostTopEdge && pacmanBottomEdge <= ghostBottomEdge)) {
                    lifeGone();
                }
            }
        }
    }

}
