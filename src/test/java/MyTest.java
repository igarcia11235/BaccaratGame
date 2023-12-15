import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MyTest {

	private Card card;
	private BaccaratDealer dealer;
	private BaccaratGameLogic gameLogic;

	@BeforeEach
	public void setup() {
		card = new Card("Hearts", 7);
		dealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();
	}

	@Test
	public void testCardConstructor() {
		assertEquals("Hearts", card.getSuite());
		assertEquals(7, card.getValue());
	}

	@Test
	public void testCardGetValue() {
		assertEquals(7, card.getValue());
	}

	@Test
	public void testGetValueForFaceCard() {
		Card card = new Card("Hearts", 11);
		assertEquals(0, card.getValue());
	}

	@Test
	public void testToStringForAce() {
		Card card = new Card("Spades", 1);
		assertEquals("ace_of_Spades", card.toString());
	}

	@Test
	public void testGenerateDeck() {
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.generateDeck();
		assertEquals(52, dealer.deckSize());
	}

	@Test
	public void testDealHand() {
		BaccaratDealer dealer = new BaccaratDealer();
		ArrayList<Card> hand = dealer.dealHand();
		assertEquals(2, hand.size());
	}

	@Test
	public void testShuffleDeck() {
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.shuffleDeck();
		// It's challenging to test shuffle results, so you can test if the deck size remains the same.
		assertEquals(52, dealer.deckSize());
	}

	@Test
	public void testBaccaratDealerGenerateDeck() {
		dealer.generateDeck();
		assertEquals(52, dealer.deckSize());
	}

	@Test
	public void testBaccaratDealerDealHand() {
		ArrayList<Card> hand = dealer.dealHand();
		assertEquals(2, hand.size());
		assertEquals(50, dealer.deckSize());
	}

	@Test
	public void testBaccaratGameLogicHandTotal() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card("Hearts", 2));
		hand.add(new Card("Diamonds", 8));
		hand.add(new Card("Clubs", 11));

		assertEquals(0, gameLogic.handTotal(hand));
	}

	@Test
	public void testBaccaratGameLogicEvaluatePlayerDraw() {
		ArrayList<Card> playerHand = new ArrayList<>();
		playerHand.add(new Card("Hearts", 3));
		playerHand.add(new Card("Diamonds", 7));

		assertTrue(gameLogic.evaluatePlayerDraw(playerHand));
	}

	@Test
	public void testBaccaratGameLogicEvaluateBankerDraw() {
		ArrayList<Card> bankerHand = new ArrayList<>();
		bankerHand.add(new Card("Hearts", 3));
		bankerHand.add(new Card("Diamonds", 7));

		assertTrue(gameLogic.evaluateBankerDraw(bankerHand, new Card("Clubs", 2)));
	}

	@Test
	public void testBaccaratGameLogicIsNatural() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card("Hearts", 6));
		hand.add(new Card("Diamonds", 2));

		assertTrue(gameLogic.isNatural(hand));
	}

	@Test
	public void testEvaluateBankerDrawNoPlayerCard() {
		BaccaratGameLogic logic = new BaccaratGameLogic();
		ArrayList<Card> hand = new ArrayList<>();
		assertTrue(logic.evaluateBankerDraw(hand, null));
	}

	@Test
	public void testHandTotal() {
		BaccaratGameLogic logic = new BaccaratGameLogic();
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card("Hearts", 2));
		hand.add(new Card("Diamonds", 3));
		assertEquals(5, logic.handTotal(hand));
	}

}
