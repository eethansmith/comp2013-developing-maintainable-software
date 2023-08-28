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

/**
 * Manager class coordinates between the game components like the Pacman, Ghosts, and Maze.
 * It is also responsible for game metrics like score, rounds, and lives.
 */
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


    /**
     * Constructor to initialize the Manager and its attributes.
     *
     * @param root the root node of the JavaFX scene graph.
     * @param gameStage the stage where the game is rendered.
     */
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
        this.powerUpUsed = false;
    }

    /**
     * Method to handle game over.
     */
    void gameOver() {
        // Setting game state to ended
        gameEnded = true;

        // Remove Pacman from the scene graph
        root.getChildren().remove(pacman);

        // Remove all Ghost objects from the scene graph
        for (Ghost ghost : ghosts) {
            root.getChildren().remove(ghost);
        }

        // Create and configure a Text object for the end game message
        javafx.scene.text.Text endGame = new javafx.scene.text.Text("Game Over, press ESC to restart");
        endGame.setX(Obstacle.THICKNESS * 3);  // Setting the X position
        endGame.setY(Obstacle.THICKNESS * 28); // Setting the Y position
        endGame.setFont(Font.font("Arial Black", 40));
        endGame.setFill(Color.ROYALBLUE);

        // Remove score and other metrics from the scene
        this.scoreBoard.removeTextsFromRoot();

        // Add the end game message to the scene
        root.getChildren().add(endGame);

        // Save the score to a CSV file
        String playerName = Manager.getName();
        saveScoreToCSV(playerName, score);

        // Transition to the ScoreboardScreen
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

    /**
     * Method to handle a scenario where the player loses a life.
     */
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

    /**
     * Method to restart the game.
     *
     * @param event the KeyEvent generated.
     */
    public void restartGame(KeyEvent event) {
        // Check if the ESC key was pressed and the game has ended
        if (event.getCode() == KeyCode.ESCAPE && gameEnded) {
            // Clear all elements from the root node
            root.getChildren().clear();

            // Clear cookies and ghosts sets
            this.cookieSet.clear();
            this.ghosts.clear();

            // Re-draw the maze
            this.drawMaze();

            // Reset Pacman's position
            pacman.setCenterX(2.5 * Obstacle.THICKNESS);
            pacman.setCenterY(2.5 * Obstacle.THICKNESS);

            // Reset lives, score, and cookies eaten
            this.lives = 3;
            this.score = 0;
            this.cookiesEaten = 0;

            // Reset the game ended flag
            gameEnded = false;
        }
    }

    /**
     * Method to proceed to the next round.
     */
    public void nextRound() {
        // Increment the round counter
        round = round + 1;

        // Remove Pacman and ghosts from the scene
        root.getChildren().remove(pacman);
        for (Ghost ghost : ghosts) {
            root.getChildren().remove(ghost);
        }

        // Clear root, cookies and ghosts sets
        root.getChildren().clear();
        this.cookieSet.clear();
        this.ghosts.clear();

        // Re-draw the maze
        this.drawMaze();

        // Reset Pacman's position
        pacman.setCenterX(2.5 * Obstacle.THICKNESS);
        pacman.setCenterY(2.5 * Obstacle.THICKNESS);

        // Reset cookies eaten counter
        this.cookiesEaten = 0;

        // Reset the game ended flag
        gameEnded = false;

        // Restart the Ghost threads and display the new round banner
        for (Ghost ghost : this.ghosts) {
            ghost.run();
            levelBanner("ROUND " + round);
        }
    }

    /**
     * Getter method for powerUpActive.
     *
     * @return true if the power-up is active, false otherwise.
     */
    public boolean isPowerUpActive() {
        return powerUpActive;
    }

    /**
     * Toggles power-up activation.
     *
     * @param event the KeyEvent generated.
     */
    public void togglePowerUp(KeyEvent event) {
        // Check if the SPACE key is pressed
        if (event.getCode() == KeyCode.SPACE) {
            // Activate the power-up only if it has not been used yet
            if (!this.powerUpUsed) {
                levelBanner("POWER UP ACTIVE!");
                scoreBoard.movePowerUpTextOffScreen();

                // Set power-up as active
                this.powerUpActive = true;

                // Apply power-up effects to each ghost
                for (Ghost ghost : ghosts) {
                    ghost.setPowerUpActive(true);
                }

                // Mark the power-up as used
                this.powerUpUsed = true;

                // Create a 7-second timer to deactivate the power-up
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(7000),
                        ae -> endPowerUp())
                );
                timeline.play();
            }
        }
    }

    /**
     * Deactivates any active power-up and resets the game state accordingly.
     * This will affect both the Manager and the individual Ghost objects.
     */
    public void endPowerUp() {
        // Deactivating power-up for the Manager
        this.powerUpActive = false;

        // Deactivate power-up effects for each ghost
        for (Ghost ghost : ghosts) {
            ghost.setPowerUpActive(false);
        }

        // Display a banner indicating that the power-up has ended
        levelBanner("POWER UP ENDED!");
    }

    /**
     * Deactivates any active power-up and resets the game state accordingly.
     * This will affect both the Manager and the individual Ghost objects.
     */
    public void levelBanner(String message) {
        // Create and configure the Text object for the banner
        Text text = new Text(message);
        text.setFont(Font.font("Arial Black", 50));
        text.setFill(Color.RED);

        // Center the text on the screen
        text.setX((1225 - text.getLayoutBounds().getWidth()) / 2);
        text.setY(600 / 2);

        // Add the text to the scene graph
        root.getChildren().add(text);

        // Pause for 3 seconds before removing the text
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            root.getChildren().remove(text);  // Remove the text after 3 seconds
        });
        pause.play();
    }

    /**
     * Moves the Pacman character based on the KeyEvent provided.
     * This also triggers the ghosts to start moving if it is Pacman's first move.
     *
     * @param event The KeyEvent generated by user input.
     */
    public void movePacman(KeyEvent event) {
        // Move Pacman based on the event received
        pacman.move(event);

        // Start ghost movement when Pacman moves for the first time
        if (firstMove == true) {
            for (Ghost ghost : this.ghosts) {
                ghost.run();
                firstMove = false;
            }
        }
    }

    /**
     * Stops the Pacman character based on the KeyEvent provided.
     *
     * @param event The KeyEvent generated by user input.
     */
    public void stopPacman(KeyEvent event) {
        // Stop Pacman based on the event received
        pacman.stop(event);
    }

    /**
     * Sets the player name for the game session. If the name is null or empty,
     * sets the name to "N/A".
     *
     * @param playerName The name of the player.
     */
    public void setPlayerName(String playerName) {
        // Check if playerName is empty or null; set to "N/A" if so
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "N/A";
        }
        // Log the received player name
        System.out.println("Received player name: " + playerName);

        // Update the instance variable with the new player name
        this.name = playerName;
    }

    /**
     * Retrieves the player's name. This is a static method, which means it's
     * callable on the class itself rather than an instance of the class.
     *
     * @return The name of the player.
     */
    public static String getName() {
        return name;
    }

    /**
     * Saves the player's score to a CSV file named "player_scores.csv".
     * If the file does not exist, it will be created. Existing scores in the file
     * will be sorted in descending order.
     *
     * @param playerName The name of the player whose score is being saved.
     * @param playerScore The score of the player to be saved.
     */
    private void saveScoreToCSV(String playerName, int playerScore) {
        File csvFile = new File("player_scores.csv");
        List<String[]> records = new ArrayList<>();
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // CSV file handling code
        try {
            // Check if the CSV file exists, create it if not
            if (!csvFile.exists()) {
                csvFile.createNewFile();

                // Write player data to the new file
                try (FileWriter csvWriter = new FileWriter(csvFile)) {
                    csvWriter.append("PlayerName,PlayerScore,Recorded\n");
                    csvWriter.append(playerName).append(",").append(Integer.toString(playerScore)).append(",").append(timeStamp).append("\n");
                }
            } else {
                // Append player data to the existing file
                try (FileWriter csvWriter = new FileWriter(csvFile, true)) {
                    csvWriter.append(playerName).append(",").append(Integer.toString(playerScore)).append(",").append(timeStamp).append("\n");
                }
            }

            // Read and store existing records
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                br.readLine();  // Skip header line
                while ((line = br.readLine()) != null) {
                    records.add(line.split(","));
                }
            }

            // Sort records by score in descending order
            records.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

            // Write sorted records back to the file
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

    /**
     * Sets the level of difficulty for the game.
     * Also, initializes the level to "normal" if null is provided.
     *
     * @param level The chosen level of difficulty.
     */
    public static void setLevel(String level) {
        Manager.level = level;  // Sets the static variable level in the Manager class to the given level
        if (level == null) {  // Checks if the provided level is null
            level = "normal";  // If it is, default to "normal"
        }
        System.out.println("You chose difficulty: -" + level);  // Outputs the chosen difficulty
    }

    /**
     * Gets the level of difficulty for the game.
     * Initializes the level to "normal" if it is null.
     *
     * @return The current level of difficulty.
     */
    public static String getLevel() {
        if (level == null) {  // Checks if level is null
            level = "normal";  // If it is, default to "normal"
        }
        return level;  // Returns the current level
    }

    /**
     * Draws the maze along with its associated components like cookies and ghosts.
     */
    public void drawMaze() {
        setupBackground();  // Calls a method to set up the background
        drawCookiesInLines();  // Calls a method to draw the cookies on the screen
        this.maze.createMaze(root);  // Generates the maze

        this.generateGhosts();  // Generates the ghosts on the screen
        root.getChildren().addAll(this.ghosts);  // Adds all the ghosts to the root node for rendering
        this.scoreBoard = new GameMetrics(root);  // Initializes the scoreboard
    }

    /**
     * Sets up the background image or color based on the chosen level of difficulty.
     */
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

    /**
     * Draws cookies in a single line, skipping positions based on the 'skip' array.
     *
     * @param lineNumber Line number in which cookies are to be drawn.
     * @param skip Array of positions where cookies should not be drawn.
     * @param yMultiplier Multiplier for y-coordinate positioning.
     */
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

    /**
     * Draws cookies in multiple lines by calling drawCookiesInLine method for each line.
     */
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

    /**
     * Generates and places the ghosts in the maze.
     */
        public void generateGhosts() {
            // Creates and adds various Ghost objects to the list
            this.ghosts.add(new Ghost(22.5 * Obstacle.THICKNESS, 12.5 * Obstacle.THICKNESS, "/example/Ghost-Green.png", maze, this, pacman));
            this.ghosts.add(new Ghost(28.5 * Obstacle.THICKNESS, 12.5 * Obstacle.THICKNESS, "/example/Ghost-Pink.png", maze, this, pacman));
            this.ghosts.add(new Ghost(28.5 * Obstacle.THICKNESS, 9.5 * Obstacle.THICKNESS, "/example/Ghost-Purple.png", maze, this, pacman));
            this.ghosts.add(new Ghost(18.5 * Obstacle.THICKNESS, 9.5 * Obstacle.THICKNESS, "/example/Ghost-Yellow.png", maze, this, pacman));
        }
    /**
     * Retrieves the set of ghosts.
     *
     * @return The set of Ghost objects.
     */
    public Set<Ghost> getGhosts() {
        return this.ghosts;  // Returns the set of Ghost objects
    }
}