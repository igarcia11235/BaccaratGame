import java.util.ArrayList;
import java.util.Collections;

public class BaccaratDealer {
    private ArrayList<Card> deck; // ArrayList to represent the deck of cards

    // Constructor for the BaccaratDealer class
    public BaccaratDealer() {
        generateDeck(); // Initialize the deck by generating a standard 52 card deck
        shuffleDeck();  // Shuffle the deck to randomize the card order
    }

    // Generates full deck in order
    public void generateDeck() {
        deck = new ArrayList<>(); // Create a new empty ArrayList for the deck
        String[] suits = { "hearts", "diamonds", "clubs", "spades" };

        // Loop through each suit and value to create the 52-card deck
        for (String suit : suits) {
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value)); // Add a new Card object to the deck
            }
        }
    }

    // Returns a copy of the deck
    public ArrayList<Card> dealHand() {
        ArrayList<Card> hand = new ArrayList<>(); // Create a new ArrayList to represent a hand of cards
        hand.add(drawOne()); // Draw the first card and add it to the hand
        hand.add(drawOne()); // Draw the second card and add it to the hand
        return hand; // Return the ArrayList representing the hand
    }

    public Card drawOne() {
        if (deckSize() == 0) {
            shuffleDeck(); // If the deck is empty, shuffle it to continue the game
        }
        return deck.remove(0); // Remove and return the top card from the deck
    }

    public void shuffleDeck() {
        Collections.shuffle(deck); // Shuffle the deck to randomize the order of the cards
    }

    public int deckSize() {
        return deck.size(); // Return the number of cards remaining in the deck
    }
}
