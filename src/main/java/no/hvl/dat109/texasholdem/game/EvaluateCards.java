package no.hvl.dat109.texasholdem.game;

import java.util.Set;

public class EvaluateCards {
    public static int compareHand(Hand hand1, Hand hand2) {
        int score1 = calculateHandScore(hand1);
        int score2 = calculateHandScore(hand2);
        return Integer.compare(score1, score2);
    }

    private static int calculateHandScore(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int[] verdiCount = new int[15]; // 2-14, 14 being Ace
        int[] typeCount = new int[4]; // Spades, Hearts, Diamonds, Clubs

        for (Kort k : kort) {
            verdiCount[k.getVerdi()]++;
            typeCount[k.getKorttype().ordinal()]++;
        }

        // Check for specific hand combinations
        if (isRoyalFlush(verdiCount, typeCount)) {
            return 10;
        } else if (isStraightFlush(verdiCount, typeCount)) {
            return 9;
        } else if (isFourOfAKind(verdiCount)) {
            return 8;
        } else if (isFullHouse(verdiCount)) {
            return 7;
        } else if (isFlush(typeCount)) {
            return 6;
        } else if (isStraight(verdiCount)) {
            return 5;
        } else if (isThreeOfAKind(verdiCount)) {
            return 4;
        } else if (isTwoPair(verdiCount)) {
            return 3;
        } else if (isPair(verdiCount)) {
            return 2;
        } else {
            return 1; // Høyt kort
        }
    }

    private static boolean isRoyalFlush(int[] verdiCount, int[] typeCount) {
        // Check for flush
        for (int i = 0; i < 4; i++) {
            if (typeCount[i] >= 5) {
                // Check for 10, J, Q, K, A
                if (verdiCount[10] > 0 && verdiCount[11] > 0 && verdiCount[12] > 0 && verdiCount[13] > 0 && verdiCount[14] > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isStraightFlush(int[] verdiCount, int[] typeCount) {
        // Check for flush
        for (int i = 0; i < 4; i++) {
            if (typeCount[i] >= 5) {
                // Check for straight
                for (int j = 1; j <= 10; j++) {
                    if (verdiCount[j] > 0 && verdiCount[j + 1] > 0 && verdiCount[j + 2] > 0 && verdiCount[j + 3] > 0 && verdiCount[j + 4] > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Metode for å sjekke om det er fire like
    private static boolean isFourOfAKind(int[] verdiCount) {
        for (int i = 2; i < 15; i++) {
            if (verdiCount[i] == 4) {
                return true;
            }
        }
        return false;
    }

    // Metode for å sjekke om det er en full house
    private static boolean isFullHouse(int[] verdiCount) {
        boolean three = false;
        boolean two = false;
        for (int i = 2; i < 15; i++) {
            if (verdiCount[i] == 3) {
                three = true;
            } else if (verdiCount[i] == 2) {
                two = true;
            }
        }
        return three && two;
    }

    // Metode for å sjekke om det er flush
    private static boolean isFlush(int[] typeCount) {
        for (int i = 0; i < 4; i++) {
            if (typeCount[i] >= 5) {
                return true;
            }
        }
        return false;
    }

    // Metode for å sjekke om det er straight
    private static boolean isStraight(int[] verdiCount) {
        for (int i = 1; i <= 10; i++) {
            if (verdiCount[i] > 0 && verdiCount[i + 1] > 0 && verdiCount[i + 2] > 0 && verdiCount[i + 3] > 0 && verdiCount[i + 4] > 0) {
                return true;
            }
        }
        return false;
    }

    // Metode for å sjekke om det er tre like
    private static boolean isThreeOfAKind(int[] verdiCount) {
        for (int i = 2; i < 15; i++) {
            if (verdiCount[i] == 3) {
                return true;
            }
        }
        return false;
    }

    // Metode for å sjekke om det er to par
    private static boolean isTwoPair(int[] verdiCount) {
        int pairs = 0;
        for (int i = 2; i < 15; i++) {
            if (verdiCount[i] == 2) {
                pairs++;
            }
        }
        return pairs >= 2;
    }

    // Metode for å sjekke om det er et par
    private static boolean isPair(int[] verdiCount) {
        for (int i = 2; i < 15; i++) {
            if (verdiCount[i] == 2) {
                return true;
            }
        }
        return false;
    }
}