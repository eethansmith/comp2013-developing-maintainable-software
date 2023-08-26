package example;  // Import your MainFX class
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardScreen extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Create a root BorderPane
        BorderPane root = new BorderPane();

        displayHighScores();

        // Set background image
        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:/MainMenuBackground.jpg", 1000, 600, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

        // Create the "Return to Main Menu" button
        Button returnToMenuButton = new Button("Return to Main Menu");
        returnToMenuButton.setStyle("-fx-text-fill: white;");
        returnToMenuButton.setFont(new Font("Arial", 24));

        // Add an action handler to the "Return to Main Menu" button
        returnToMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Close the current window
                primaryStage.close();

                // Launch the Main Menu
                Platform.runLater(() -> MainFX.launchMainMenu(new Stage()));
            }
        });

        // Create a HBox and add the button to it
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);  // Center the button
        hbox.getChildren().add(returnToMenuButton);

        // Add the HBox to the bottom of the root BorderPane
        root.setBottom(hbox);

        // Create the scene and add the root BorderPane
        Scene scene = new Scene(root, 1000, 600);

        // Set the stage
        primaryStage.setTitle("Scoreboard Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void displayHighScores() {
        File csvFile = new File("player_scores.csv");
        List<String[]> records = new ArrayList<>();

        try {
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

            // Sort records based on timestamp to find the most recent
            List<String[]> recordsCopy = new ArrayList<>(records);
            recordsCopy.sort((a, b) -> Long.compare(Long.parseLong(b[2]), Long.parseLong(a[2])));
            String[] mostRecentRecord = recordsCopy.get(0); // Get the most recent record

            // Display the top 3 scores
            System.out.println("HIGHSCORES:");
            int rank = 1;
            int yourScoreRank = -1;
            for (String[] record : records) {
                if (record[0].equals(mostRecentRecord[0]) && record[1].equals(mostRecentRecord[1])) {
                    yourScoreRank = rank;
                }
                if (rank <= 3) {
                    System.out.println(rank + ". " + record[0] + " - " + record[1]);
                }
                rank++;
            }

            // If less than 3 scores are available, fill up with N/A and 0
            while (rank <= 3) {
                System.out.println(rank + ". " + "N/A - 0");
                rank++;
            }

            if (yourScoreRank != -1) {
                System.out.println(" ");
                System.out.println("Your Score: ");
                System.out.println(yourScoreRank + ". " + mostRecentRecord[0] + " - " + mostRecentRecord[1]);
            } else {
                System.out.println(" ");
                System.out.println("Your Score: ");
                System.out.println(mostRecentRecord[0] + " - " + mostRecentRecord[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
