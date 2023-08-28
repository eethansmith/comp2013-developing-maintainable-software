package example;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Manager {

    private Pacman pacman;
    private Group root;
    Set<Cookie> cookieSet;
    Set<Ghost> ghosts;
    private Maze maze;
    private int lives;
    int score;
    int round;
    GameMetrics scoreBoard;
    boolean gameEnded;
    int cookiesEaten;
    boolean firstMove;
    private static String name;
    private Stage gameStage;
    private static String level;
    private boolean powerUpActive;
    private boolean powerUpUsed;  // Added this line


    Manager(Group root, Stage gameStage) {
        this.root = root;
        this.gameStage = gameStage;
        this.maze = new Maze(this);
        this.cookieSet = new HashSet<>();
        this.ghosts = new HashSet<>();
        this.lives = 3;
        this.score = 0;
        this.round = 1;
        this.firstMove = true;
        this.cookiesEaten = 0;
        this.pacman = new Pacman(2.5 * Obstacle.THICKNESS, 2.5 * Obstacle.THICKNESS, this.maze, this, ghosts, root);
        levelBanner("ROUND " + round);
        javafx.scene.text.Text powerUp = new javafx.scene.text.Text("Press 'SPACE' for one time use POWER UP");
        this.powerUpUsed = false;

    }

    void lifeGone() {
        pacman.stopAllAnimations();
        this.pacman.setCenterX(2.5 * Obstacle.THICKNESS);
        this.pacman.setCenterY(2.5 * Obstacle.THICKNESS);
        lives--;
        score -= 10;
        this.scoreBoard.updateScore(this.score);
        this.scoreBoard.updateLives(this.lives);
        levelBanner("LIVES -1");
        if (lives == 0) {
            this.gameOver();
        }
    }
    void gameOver() {
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
            pacman.setCenterX(2.5 * Obstacle.THICKNESS);
            pacman.setCenterY(2.5 * Obstacle.THICKNESS);
            this.lives = 3;
            this.score = 0;
            this.cookiesEaten = 0;
            gameEnded = false;
        }
    }

    public void nextRound() {
        round = round + 1;
        root.getChildren().remove(pacman);
        for (Ghost ghost : ghosts) {
            root.getChildren().remove(ghost);
        }
        root.getChildren().clear();
        this.cookieSet.clear();
        this.ghosts.clear();
        this.drawMaze();
        pacman.setCenterX(2.5 * Obstacle.THICKNESS);
        pacman.setCenterY(2.5 * Obstacle.THICKNESS);
        this.cookiesEaten = 0;
        gameEnded = false;
        for (Ghost ghost : this.ghosts) {
            ghost.run();
            this.scoreBoard.updateRound(round);
            levelBanner("ROUND " + round);
        }
    }

    public boolean isPowerUpActive() {
        return powerUpActive;
    }

    public void togglePowerUp(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            // Activate the power-up only if it has not been used yet
            if (!this.powerUpUsed) {
                levelBanner("POWER UP ACTIVE!");

                this.powerUpActive = true; // Activating power-up for the Manager

                // Now activating power-up for each Ghost
                for (Ghost ghost : ghosts) {
                    ghost.setPowerUpActive(true);  // This will set the powerUpActive in each Ghost to true
                }

                // Mark the power-up as used
                this.powerUpUsed = true;

                // Create a Timeline object to deactivate the power-up after 7 seconds
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(7000), // 7 seconds
                        ae -> endPowerUp())
                );
                timeline.play();
            }
        }
    }
    public void endPowerUp() {
        this.powerUpActive = false;  // Deactivating power-up for the Manager
        for (Ghost ghost : ghosts) {
            ghost.setPowerUpActive(false);
        }
        levelBanner("POWER UP ENDED!");
    }



    public void levelBanner(String message) {
        Text text = new Text(message);
        text.setFont(Font.font("Arial Black", 50));
        text.setFill(Color.RED);

        // Center the text
        text.setX((1225 - text.getLayoutBounds().getWidth()) / 2);
        text.setY(600 / 2);

        root.getChildren().add(text);

        // Pause for 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            root.getChildren().remove(text); // Remove the text
        });
        pause.play();
    }

    public void movePacman(KeyEvent event) {
        pacman.move(event);
        if (firstMove == true){
            for (Ghost ghost : this.ghosts) {
                ghost.run();
                firstMove = false;
            }
        }
    }

    public void stopPacman(KeyEvent event) {
        pacman.stop(event);
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

    public static void setLevel(String level) {

        Manager.level = level;
        if (level == null) {
            level = "normal";
        }
        System.out.println("You chose difficulty: -" + level);
    }

    public static String getLevel() {
        if (level == null) {
            level = "normal";
        }
        return level;
    }

    public void drawMaze() {
        setupBackground();
        drawCookiesInLines();
        this.maze.createMaze(root);

        this.generateGhosts();
        root.getChildren().addAll(this.ghosts);
        this.scoreBoard = new GameMetrics(root);
    }

    private void setupBackground() {
        Image backgroundImage = new Image(getClass().getResource("/example/MainMenuBackground.jpg").toExternalForm(), 1225, 600, false, true);
        ImagePattern imagePattern = new ImagePattern(backgroundImage);
        Rectangle rect = new Rectangle(0, 0, 1225, 600);
        if (level == "normal") {
            rect.setFill(imagePattern);
        }
        if (level == "hard") {
            rect.setFill(Color.BLACK);
        }
        root.getChildren().add(0, rect);
    }

    private void drawCookiesInLine(int lineNumber, Integer[] skip, double yMultiplier) {
        Set<Integer> skipSet = new HashSet<>(Arrays.asList(skip));
        for (int i = 0; i < 23; i++) {
            if (!skipSet.contains(i)) {
                Cookie cookie = new Cookie(((2 * i) + 2.5) * Obstacle.THICKNESS, yMultiplier * Obstacle.THICKNESS);
                this.cookieSet.add(cookie);
                root.getChildren().add(cookie);

            }
        }
    }
    private void drawCookiesInLines() {
        root.getChildren().add(pacman);
        drawCookiesInLine(1, new Integer[]{5, 17}, 2.5);
        drawCookiesInLine(2, new Integer[]{1, 2, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 19, 20, 21}, 4.5);
        drawCookiesInLine(3, new Integer[]{1, 21}, 6.5);
        drawCookiesInLine(4, new Integer[]{1, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 21}, 8.5);
        drawCookiesInLine(5, new Integer[]{1, 7, 8, 9, 10, 11, 12, 13, 14, 15, 21}, 10.5);
        drawCookiesInLine(6, new Integer[]{3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19}, 12.5);
        drawCookiesInLine(7, new Integer[]{1, 7, 8, 9, 10, 11, 12, 13, 14, 15, 21}, 14.5);
        drawCookiesInLine(8, new Integer[]{1, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 21}, 16.5);
        drawCookiesInLine(9, new Integer[]{1, 21}, 18.5);
        drawCookiesInLine(10, new Integer[]{1, 2, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 19, 20, 21}, 20.5);
        drawCookiesInLine(11, new Integer[]{5, 17}, 22.5);
    }

        public void generateGhosts() {
        this.ghosts.add(new Ghost(22.5 * Obstacle.THICKNESS, 12.5 * Obstacle.THICKNESS, "/example/Ghost-Green.png", maze, this, pacman));
        this.ghosts.add(new Ghost(28.5 * Obstacle.THICKNESS, 12.5 * Obstacle.THICKNESS, "/example/Ghost-Pink.png", maze, this, pacman));
        this.ghosts.add(new Ghost(28.5 * Obstacle.THICKNESS, 9.5 * Obstacle.THICKNESS, "/example/Ghost-Purple.png", maze, this, pacman));
        this.ghosts.add(new Ghost(18.5 * Obstacle.THICKNESS, 9.5 * Obstacle.THICKNESS, "/example/Ghost-Yellow.png", maze, this, pacman));
    }
    public Set<Ghost> getGhosts() {
        return this.ghosts;
    }
}
