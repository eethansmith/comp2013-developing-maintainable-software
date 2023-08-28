package example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A JavaFX Application class responsible for displaying the scoreboard screen.
 */
public class ScoreboardScreen extends Application {

    /**
     * Entry point for the JavaFX application.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox scoreBox = new VBox();
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setSpacing(10);  // Reduced spacing between elements

        // Populate the scoreBox with high scores
        displayHighScores(scoreBox);

        // Background image setup
        Image backgroundImage = new Image(getClass().getResource("/example/MainMenuBackground.jpg").toExternalForm(), 1000, 600, false, true);
        root.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        // Set the scoreBox in the center of the root layout
        root.setCenter(scoreBox);

        // Create menu and exit buttons
        Label MenuLabel = new Label("MENU");
        MenuLabel.getStyleClass().add("button-label");
        Button MenuButton = new Button();
        MenuButton.setGraphic(MenuLabel);
        MenuButton.getStyleClass().add("styled-button");
        MenuButton.setOnAction(e -> {
            primaryStage.close();
            Platform.runLater(() -> MainFX.launchMainMenu(new Stage()));
        });

        // Create a Label
        Label ExitLabel = new Label("EXIT GAME");
        ExitLabel.getStyleClass().add("button-label");

        // Create a Button and set the label as its graphic content
        Button ExitButton = new Button();
        ExitButton.setGraphic(ExitLabel);
        ExitButton.getStyleClass().add("styled-button");
        ExitButton.setOnAction(e -> Platform.exit());

        // Arrange buttons in a VBox
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(3);
        vbox.getChildren().addAll(MenuLabel, ExitLabel, MenuButton, ExitButton);

        // Add buttons to the bottom of the root layout
        root.setBottom(vbox);

        // Final stage and scene setup
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Scoreboard Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/example/styles.css").toExternalForm());
    }

    /**
     * Displays high scores on the scoreboard screen.
     *
     * @param scoreBox The VBox where the high scores are to be displayed.
     */
    public void displayHighScores(VBox scoreBox) {
        // Your code for reading the file and filling the scoreBox
        File csvFile = new File("player_scores.csv");
        List<String[]> records = new ArrayList<>();

        try {
            // Read records from file
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                // Sort records
                String line;
                // Skip the header line
                br.readLine();
                while ((line = br.readLine()) != null) {
                    records.add(line.split(","));
                }
            }

            // Get the most recent record
            records.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

            // Sort records based on timestamp to find the most recent
            List<String[]> recordsCopy = new ArrayList<>(records);
            recordsCopy.sort((a, b) -> Long.compare(Long.parseLong(b[2]), Long.parseLong(a[2])));
            String[] mostRecentRecord = recordsCopy.get(0); // Get the most recent record

            // Display the top 3 scores
            // Create and add high score labels to the VBox
            Text headerLabel = new Text("HIGHSCORES:");
            headerLabel.getStyleClass().add("header-label");
            scoreBox.getChildren().add(headerLabel);

            int rank = 1;
            int yourScoreRank = -1;

            for (String[] record : records) {
                if (record[0].equals(mostRecentRecord[0]) && record[1].equals(mostRecentRecord[1])) {
                    yourScoreRank = rank;
                }
                if (rank <= 3) {
                    Label label = new Label(rank + ". " + record[0] + " - " + record[1]);
                    label.getStyleClass().add("score-label");
                    scoreBox.getChildren().add(label);
                    scoreBox.getStyleClass().add("score-box");
                }
                rank++;
            }

            // If less than 3 scores are available, fill up with N/A and 0
            while (rank <= 3) {
                Label label = new Label(rank + ". " + "N/A - 0");
                label.setId("highscoreLabel");
                scoreBox.getChildren().add(label);
                rank++;
            }
            Label spacerLabel = new Label("");
            spacerLabel.setId("spacerLabel");
            scoreBox.getChildren().add(spacerLabel);

            Text yourScoreText = new Text("Your Score:");
            yourScoreText.setId("yourScoreText");
            scoreBox.getChildren().add(yourScoreText);

            if (yourScoreRank != -1) {
                Label scoreLabel = new Label(yourScoreRank + ". " + mostRecentRecord[0] + " - " + mostRecentRecord[1]);
                scoreLabel.getStyleClass().add("score-label");
                scoreBox.getChildren().add(scoreLabel);
            } else {
                Label scoreLabel = new Label("N/A");
                scoreLabel.getStyleClass().add("score-label");
                scoreBox.getChildren().add(scoreLabel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
