package no.hvl.dat109.texasholdem.game;

import java.util.HashSet;
import java.util.Set;

public class Hand {
    private Set<Kort> hand;

    public Hand() {
        hand = new HashSet<>();
    }


    public void addCard(Kort card) {
        hand.add(card);
    }

    public void removeCard(Kort card) {
        hand.remove(card);
    }

    public void clear() {
        hand.clear();
    }
}