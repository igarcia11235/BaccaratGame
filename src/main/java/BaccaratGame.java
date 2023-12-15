import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BaccaratGame extends Application {
    private ArrayList<Card> playerHand;
    private ArrayList<Card> bankerHand;
    private BaccaratDealer theDealer;
    private BaccaratGameLogic gameLogic;
    private double currentBet;
    private double totalWinnings;

    // Labels to display player's and banker's cards
    private Label playerCardLabel;
    private Label bankerCardLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Baccarat Game");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);

        // Create GUI elements
        VBox displayArea = new VBox(10);
        displayArea.setPadding(new Insets(10));
        TextArea resultsArea = new TextArea();
        Button playButton = new Button("Play");
        TextField betAmountField = new TextField();
        ChoiceBox<String> betChoiceBox = new ChoiceBox<>();
        betChoiceBox.getItems().addAll("The Player", "The Banker", "Draw");
        Label winningsLabel = new Label("Total Winnings: $0.0");

        // Create a HBox for displaying the cards above the "Play" button
        HBox cardDisplayBox = new HBox(10);

        // Add a label for entering the betting amount
        Label betLabel = new Label("Enter betting amount here:");

        // Add GUI elements to the layout
        displayArea.getChildren().addAll(resultsArea, cardDisplayBox, playButton, betLabel, betAmountField, betChoiceBox, winningsLabel);
        root.setCenter(displayArea);

        // Create a MenuBar with an "Options" menu
        MenuBar menuBar = new MenuBar();
        Menu optionsMenu = new Menu("Options");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem freshStartItem = new MenuItem("Fresh Start");
        optionsMenu.getItems().addAll(exitItem, freshStartItem);
        menuBar.getMenus().add(optionsMenu);
        root.setTop(menuBar);

        // Initialize Baccarat components
        theDealer = new BaccaratDealer();
        gameLogic = new BaccaratGameLogic();
        playerHand = new ArrayList<>();
        bankerHand = new ArrayList<>();

        // Initialize labels to display player's and banker's cards
        playerCardLabel = new Label("Player's Cards: ");
        bankerCardLabel = new Label("Banker's Cards: ");
        VBox cardLabels = new VBox(playerCardLabel, bankerCardLabel);
        cardDisplayBox.getChildren().addAll(cardLabels);

        // Event handler for the Play button
        playButton.setOnAction(event -> {
            try {
                currentBet = Double.parseDouble(betAmountField.getText()); // Get the bet amount
                String betChoice = betChoiceBox.getValue(); // Get the bet choice (Who the user is betting on)

                playerHand = theDealer.dealHand(); // Deal the player's hand
                bankerHand = theDealer.dealHand(); // Deal the banker's hand

                // Clear the labels for the previous round
                playerCardLabel.setText("Player's Cards: ");
                bankerCardLabel.setText("Banker's Cards: ");

                // Evaluate the winner, considering natural wins
                String result = gameLogic.whoWon(playerHand, bankerHand);

                // Check for natural wins
                if (gameLogic.isNatural(playerHand) || gameLogic.isNatural(bankerHand)) {
                    if (gameLogic.isNatural(playerHand) && gameLogic.isNatural(bankerHand)) {
                        result = "Natural Tie";
                    } else if (gameLogic.isNatural(playerHand)) {
                        result = "Natural Player";
                    } else {
                        result = "Natural Banker";
                    }
                }

                // Check if the player should draw
                boolean playerDraw = gameLogic.evaluatePlayerDraw(playerHand);
                if (playerDraw) {
                    playerHand.add(theDealer.drawOne());
                }

                // Check if the banker should draw
                boolean bankerDraw = gameLogic.evaluateBankerDraw(bankerHand, playerHand.get(0));
                if (bankerDraw) {
                    bankerHand.add(theDealer.drawOne());
                }

                double winnings = evaluateWinnings(betChoice, result);

                // Update results and winnings
                resultsArea.appendText("Player Total: " + gameLogic.handTotal(playerHand) +
                        " Banker Total: " + gameLogic.handTotal(bankerHand) + "\n");

                if (result.equals("Player")) {
                    resultsArea.appendText("Player wins\n");
                } else if (result.equals("Banker")) {
                    resultsArea.appendText("Banker wins\n");
                } else if (result.equals("Draw")) {
                    resultsArea.appendText("Draw\n");
                } else if (result.equals("Natural Player")) {
                    resultsArea.appendText("Natural Player\n");
                } else if (result.equals("Natural Banker")) {
                    resultsArea.appendText("Natural Banker\n");
                } else if (result.equals("Natural Tie")) {
                    resultsArea.appendText("Natural Tie\n");
                }

                // Display the result of the bet
                if (winnings > 0) {
                    resultsArea.appendText("Congrats, you bet " + betChoice + "! You win!\n");
                } else if (winnings < 0) {
                    resultsArea.appendText("Sorry, you bet " + betChoice + "! You lost your bet!\n");
                } else {
                    resultsArea.appendText("It's a tie, your bet is returned!\n");
                }

                totalWinnings += evaluateWinnings(betChoice, result);
                winningsLabel.setText("Total Winnings: $" + totalWinnings);

                // Display the player's and banker's cards
                displayHand(playerHand, playerCardLabel);
                displayHand(bankerHand, bankerCardLabel);

            } catch (NumberFormatException e) {
                resultsArea.appendText("Invalid bet amount. Please enter a valid bet.\n");
            }
        });

        // Event handler for the "Exit" menu item
        exitItem.setOnAction(event -> {
            System.exit(0);
        });

        // Event handler for the "Fresh Start" menu item
        freshStartItem.setOnAction(event -> {
            totalWinnings = 0;
            winningsLabel.setText("Total Winnings: $0.0");
            resultsArea.clear(); // Clear the results area
            playerHand.clear(); // Clear player's hand
            bankerHand.clear(); // Clear banker's hand
            playerCardLabel.setText("Player's Cards: ");
            bankerCardLabel.setText("Banker's Cards: ");
        });

        primaryStage.show();
    }

    public double evaluateWinnings(String betChoice, String result) {
        double winnings = 0.0;

        // Handle natural wins
        if (result.equals("Natural Player") && betChoice.equals("The Player")) {
            winnings = currentBet;
        } else if (result.equals("Natural Banker") && betChoice.equals("The Banker")) {
            winnings = currentBet * 0.95; // User wins 95% of their bet on "Banker"
        } else if (result.equals("Natural Tie") && betChoice.equals("Draw")) {
            winnings = currentBet * 8; // Pay 8:1 on a successful "Draw" bet
        } else {
            // Handle non-natural wins or losses
            if (result.equals("Player") && betChoice.equals("The Player")) {
                winnings = currentBet;
            } else if (result.equals("Banker") && betChoice.equals("The Banker")) {
                winnings = currentBet * 0.95;
            } else if (result.equals("Draw") && betChoice.equals("Draw")) {
                winnings = currentBet * 8;
            } else {
                winnings = -currentBet;
            }
        }

        return winnings;
    }

    // Helper method to display all the cards in a hand
    /*private void displayHand(ArrayList<Card> hand, Label label) {
        StringBuilder cardsText = new StringBuilder(label.getText() + " ");
        for (Card card : hand) {
            cardsText.append(card.toString()).append(" ");
        }
        label.setText(cardsText.toString());
    }*/

    private void displayHand(ArrayList<Card> hand, Label label) {
        HBox cardImages = new HBox(); // Create an HBox to hold card images

        for (Card card : hand) {
            // Load the image for the card
            ImageView cardImageView = new ImageView(new Image("file:///images/" + card.toString() + ".png"));
            cardImageView.setFitHeight(150); // Set the height of the image
            cardImageView.setFitWidth(100); // Set the width of the image

            cardImages.getChildren().add(cardImageView); // Add the image to the HBox
        }

        label.setGraphic(cardImages); // Set the HBox containing images as the graphic of the label
    }

}
