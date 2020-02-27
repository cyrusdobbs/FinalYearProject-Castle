package model.cards.card;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum Suit {

    HEARTS (0, "H"),
    DIAMONDS (1, "D"),
    SPADES (2, "S"),
    CLUBS (3, "C");

    private int suitCode;
    private String shortName;

    Suit(int suitCode, String shortName) {
        this.suitCode = suitCode;
        this.shortName = shortName;
    }

    public int getSuitCode() {
        return suitCode;
    }

    public String getShortName() {
        return shortName;
    }

    public static Set<Suit> getSuits() {
        return new HashSet<>(EnumSet.allOf(Suit.class));
    }
}
