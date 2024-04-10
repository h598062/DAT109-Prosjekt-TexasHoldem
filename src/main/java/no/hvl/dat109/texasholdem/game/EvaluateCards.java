package no.hvl.dat109.texasholdem.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class EvaluateCards {

    private static final Map<Integer, Function<Hand, Integer>> scoreToHighestCardFunction = new HashMap<>();


    public static int compareHand(Hand hand1, Hand hand2) {
        int score1 = calculateHandScore(hand1);
        int score2 = calculateHandScore(hand2);

        if (score1 == score2 && scoreToHighestCardFunction.containsKey(score1)) {
            return Integer.compare(scoreToHighestCardFunction.get(score1).apply(hand1), scoreToHighestCardFunction.get(score2).apply(hand2));
        }

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
        } else if (isHighCard(verdiCount)) {
            return 1;
        } else {
            return 0;
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

    //Metode for å sjekke om det er høyt kort
    private static boolean isHighCard(int[] verdiCount) {
        for (int i = 2; i < 15; i++) {
            if (verdiCount[i] == 1) {
                return true;
            }
        }
        return false;
    }


    static {
        scoreToHighestCardFunction.put(1, EvaluateCards::getHighestCard);
        scoreToHighestCardFunction.put(2, EvaluateCards::getHighestPair);
        scoreToHighestCardFunction.put(3, EvaluateCards::getHighestTwoPair);
        scoreToHighestCardFunction.put(4, EvaluateCards::getHighestThreeOfAKind);
        scoreToHighestCardFunction.put(5, EvaluateCards::getHighestStraight);
        scoreToHighestCardFunction.put(6, EvaluateCards::getHighestFlush);
        scoreToHighestCardFunction.put(7, EvaluateCards::getHighestFullHouse);
        scoreToHighestCardFunction.put(8, EvaluateCards::getHighestFourOfAKind);
        scoreToHighestCardFunction.put(9, EvaluateCards::getHighestStraightFlush);
        scoreToHighestCardFunction.put(10, EvaluateCards::getHighestRoyalFlush);
    }

    // Metode for å finne høyeste royal flush
    private static Integer getHighestRoyalFlush(Hand hand) {
        return 14;
    }

    // Metode for å finne høyeste straight flush
    private static Integer getHighestStraightFlush(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestStraightFlush = 0;
        for (Kort k : kort) {
            if (k.getVerdi() > highestStraightFlush) {
                highestStraightFlush = k.getVerdi();
            }
        }
        return highestStraightFlush;
    }

    // metode for å finne høyeste fire like
    private static Integer getHighestFourOfAKind(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestFour = 0;
        for (Kort k : kort) {
            if (k.getVerdi() > highestFour) {
                highestFour = k.getVerdi();
            }
        }
        return highestFour;
    }

    // Metode for å finne høyeste full house
    private static Integer getHighestFullHouse(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestThree = 0;
        int highestPair = 0;
        for (Kort k : kort) {
            if (k.getVerdi() > highestThree) {
                highestPair = highestThree;
                highestThree = k.getVerdi();
            } else if (k.getVerdi() > highestPair) {
                highestPair = k.getVerdi();
            }
        }
        return highestThree;
    }

    // Metode for å finne høyeste flush
    private static Integer getHighestFlush(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestFlush = 0;
        for (Kort k : kort) {
            if (k.getVerdi() > highestFlush) {
                highestFlush = k.getVerdi();
            }
        }
        return highestFlush;
    }

    // Metode for å finne høyeste straight
    private static Integer getHighestStraight(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestStraight = 0;
        for (Kort k : kort) {
            if (k.getVerdi() > highestStraight) {
                highestStraight = k.getVerdi();
            }
        }
        return highestStraight;
    }

    // Metode for å finne høyeste tre like
    private static Integer getHighestThreeOfAKind(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestThree = 0;
        for (Kort k : kort) {
            if (k.getVerdi() > highestThree) {
                highestThree = k.getVerdi();
            }
        }
        return highestThree;
    }

    private static Integer getHighestTwoPair(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestPair = 0;
        int secondHighestPair = 0;
        int[] verdiCount = new int[15]; // 2-14, 14 being Ace

        for (Kort k : kort) {
            verdiCount[k.getVerdi()]++;
        }

        for (int i = 14; i >= 2; i--) {
            if (verdiCount[i] == 2) {
                if (i > highestPair) {
                    secondHighestPair = highestPair;
                    highestPair = i;
                } else if (i > secondHighestPair) {
                    secondHighestPair = i;
                }
            }
        }

        return highestPair * 15 + secondHighestPair; // Multiply by 15 because the highest card value is 14
    }

    // Metode for å finne høyeste par
    private static Integer getHighestPair(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestPair = 0;
        for (Kort k : kort) {
            if (k.getVerdi() > highestPair) {
                highestPair = k.getVerdi();
            }
        }
        return highestPair;
    }

    // Metode for å finne høyeste kort
    private static Integer getHighestCard(Hand hand) {
        Set<Kort> kort = hand.getHand();
        int highestCard = 0;
        for (Kort k : kort) {
            if (k.getVerdi() > highestCard) {
                highestCard = k.getVerdi();
            }
        }
        return highestCard;
    }
}