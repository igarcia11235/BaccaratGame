import java.util.ArrayList;

public class BaccaratGameLogic {

    /**
     * Determines the winner of the Baccarat game based on the given hands.
     *
     * @param playerHand The player's hand.
     * @param bankerHand The banker's hand.
     * @return A string representing the winner: "Player", "Banker", or "Draw".
     */
    public String whoWon(ArrayList<Card> playerHand, ArrayList<Card> bankerHand) {
        int t1 = handTotal(playerHand);
        int t2 = handTotal(bankerHand);

        if (t1 > t2) {
            return "Player";
        } else if (t1 < t2) {
            return "Banker";
        } else {
            return "Draw";
        }
    }

    /**
     * Calculates the total value of a hand in Baccarat.
     *
     * @param hand The hand to calculate the total for.
     * @return The total points of the hand.
     */
    public int handTotal(ArrayList<Card> hand) {
        int count = 0;

        for (Card card : hand) {
            if (card.getValue() == 10 ||
                    card.getValue() == 11 ||
                    card.getValue() == 12 ||
                    card.getValue() == 13) {
                continue;
            }
            count = count + card.getValue();
        }

        // if the hand is greater than 20, subtract 20. if the hand is greater than 10, subtract 10 as well...
        if (count >= 20) return (count - 20);
        else if (count >= 10) return (count - 10);
        else return count;
    }

    /**
     * Determines if the banker should draw a third card based on the current hand.
     *
     * //@param bankerHand The banker's hand.
     * @param playerCard The first card of the player's hand.
     * @return True if the banker should draw a third card, false otherwise.
     */
    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard) {
        int t = handTotal(hand);

        if (playerCard == null) {
            return t != 6 && t != 7 && t != 8 && t != 9;
        }

        if (t <= 2) {
            return true;
        } // banker draws if sum is 0,1,2

        else if (t >= 7) {
            return false;
        } // banker stands if sum is 7,8,9

        else if (t == 3) {
            return playerCard.getValue() != 8;
        } // banker draws if player card is not 8

        else if (t == 4) {
            // banker draws if player card is not 0,1,8,9
            return playerCard.getValue() != 0 &&
                    playerCard.getValue() != 1 &&
                    playerCard.getValue() != 8 &&
                    playerCard.getValue() != 9;
        } else if (t == 5) {
            // banker draws if player card is not 0,1,2,3,8,9
            return playerCard.getValue() != 0 &&
                    playerCard.getValue() != 1 &&
                    playerCard.getValue() != 2 &&
                    playerCard.getValue() != 3 &&
                    playerCard.getValue() != 8 &&
                    playerCard.getValue() != 9;
        } else {
            // banker stands if player card is not 6,7
            return playerCard.getValue() == 6 ||
                    playerCard.getValue() == 7;
        }
    }

    /**
     * Determines if the player should draw a third card based on the current hand.
     *
     * @param playerHand The player's hand.
     * @return True if the player should draw a third card, false otherwise.
     */
    public boolean evaluatePlayerDraw(ArrayList<Card> playerHand) {
        int playerTotal = handTotal(playerHand);
        return (playerTotal <= 5);
    }

    /**
     * Determines if a hand is a natural (8 or 9).
     *
     * @param hand The hand to check.
     * @return True if it's a natural, false otherwise.
     */
    public boolean isNatural(ArrayList<Card> hand) {
        return handTotal(hand) == 8 || handTotal(hand) == 9;
    }
}
