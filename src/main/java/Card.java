public class Card {
    private String suite; // Represents the suit of the card:"Spades," "Hearts," "Diamonds," "Clubs"
    private int value; // Represents the value (rank) of the card, ranging from 1 to 13 (e.g., Ace, 2, 3, ..., Queen, King)

    // Constructor for the Card class
    public Card(String theSuite, int theValue) {
        suite = theSuite; // Initialize the 'suite' instance variable with the provided suite value
        value = theValue; // Initialize the 'value' instance variable with the provided value
    }

    // Getter methods for the 'suite' and 'value' instance variables
    public String getSuite() {
        return suite;
    }

    public int getValue() {
        if (value >= 10) {
            return 0; // Face cards (10, Jack, Queen, King) and jokers count as zero
        } else {
            return value;
        }
    }

    // Custom toString() method to represent the card as a string
    @Override
    public String toString() {
        if (value == 1) {
            return "ace_of_" + suite;
        } else if (value == 11) {
            return "king_of_" + suite;
        } else if (value == 12) {
            return "queen_of_" + suite;
        } else if (value == 13) {
            return "joker_of_" + suite;
        } else {
            return value + "_of_" + suite;
        }
    }
}
