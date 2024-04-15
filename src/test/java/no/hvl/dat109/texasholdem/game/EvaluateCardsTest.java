package no.hvl.dat109.texasholdem.game;

import no.hvl.dat109.texasholdem.enums.Korttype;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EvaluateCardsTest {

	@Test
	void testGetHighestCard() throws Exception {
		Hand hand = new Hand();
		hand.addCard(new Kort(Korttype.HJERTE, 10));
		hand.addCard(new Kort(Korttype.HJERTE, 2));
		hand.addCard(new Kort(Korttype.HJERTE, 14));
		hand.addCard(new Kort(Korttype.HJERTE, 5));
		hand.addCard(new Kort(Korttype.HJERTE, 8));

		// Use reflection to access the private method
		Method method = EvaluateCards.class.getDeclaredMethod("getHighestCard", Hand.class);
		method.setAccessible(true);

		int highestCard = (int) method.invoke(null, hand);

		assertEquals(14, highestCard, "høyeste kort skal være 14 ");
	}

	@Test
	void testGameLogic() {
		// Create hands for two players
		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		// Add five same cards (on table) to both hands
		for (int i = 2; i <= 6; i++) {
			hand1.addCard(new Kort(Korttype.HJERTE, i));
			hand2.addCard(new Kort(Korttype.HJERTE, i));
		}

		// Add two unique cards to each hand
		hand1.addCard(new Kort(Korttype.HJERTE, 10));
		hand1.addCard(new Kort(Korttype.HJERTE, 11));

		hand2.addCard(new Kort(Korttype.HJERTE, 12));
		hand2.addCard(new Kort(Korttype.HJERTE, 13));

		// Use game logic to evaluate hands
		int result = EvaluateCards.compareHand(hand1, hand2);

		// Check that the result is as expected
		assertTrue(result < 0, "Hand 2 skal være vinneren");
	}

	@Test
	void testGameLogicWithTableCards() {
		// Create hands for two players and the table
		Hand tableHand = new Hand();
		Hand hand1     = new Hand();
		Hand hand2     = new Hand();

		// Add five cards to the table hand
		for (int i = 2; i <= 6; i++) {
			tableHand.addCard(new Kort(Korttype.HJERTE, i));
		}

		// Add two unique cards to each player's hand
		hand1.addCard(new Kort(Korttype.HJERTE, 10));
		hand1.addCard(new Kort(Korttype.HJERTE, 11));

		hand2.addCard(new Kort(Korttype.HJERTE, 12));
		hand2.addCard(new Kort(Korttype.HJERTE, 13));

		// Combine table cards with player's cards
		hand1.getHand().addAll(tableHand.getHand());
		hand2.getHand().addAll(tableHand.getHand());

		// Use game logic to evaluate hands
		int result = EvaluateCards.compareHand(hand1, hand2);

		// Check that the result is as expected
		assertTrue(result < 0, "Hand2 skal vinne");
	}

	@Test
	void testHighestPairWithTableCards() {

		Hand hand1 = new Hand();
		Hand hand2 = new Hand();

		hand1.addCard(new Kort(Korttype.HJERTE, 10));
		hand1.addCard(new Kort(Korttype.KLOVER, 10));

		hand2.addCard(new Kort(Korttype.HJERTE, 11));
		hand2.addCard(new Kort(Korttype.KLOVER, 11));

		List<Kort> bordKort = new ArrayList<>();

		bordKort.add(new Kort(Korttype.HJERTE, 3));
		bordKort.add(new Kort(Korttype.SPAR, 4));
		bordKort.add(new Kort(Korttype.RUTER, 5));
		bordKort.add(new Kort(Korttype.RUTER, 6));
		bordKort.add(new Kort(Korttype.HJERTE, 7));

		// Add table cards to player's hands
		hand1.getHand().addAll(bordKort);
		hand2.getHand().addAll(bordKort);

		// Use game logic to evaluate hands
		int result = EvaluateCards.compareHand(hand1, hand2);

		// Check that the result is as expected
		assertTrue(result < 0, "Hånd 2 skal vinne");
	}


}
