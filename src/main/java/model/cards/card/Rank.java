package model.cards.card;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum Rank {

    TWO (2,  11,"2"),
    THREE (3, 0, "3"),
    FOUR (4, 1,"4"),
    FIVE (5, 2,"5"),
    SIX (6, 3, "6"),
    SEVEN (7, 4,"7"),
    EIGHT (8, 5,"8"),
    NINE (9, 6,"9"),
    TEN (10, 12,"10"),
    JACK (11, 7,"J"),
    QUEEN (12, 8, "Q"),
    KING (13, 9,"K"),
    ACE (14, 10,"A");

    private int valueCode;
    private int strength;
    private String shortName;

    Rank(int value, int strength, String shortName) {
        this.valueCode = value;
        this.strength = strength;
        this.shortName = shortName;
    }

    public int getValueCode() {
        return valueCode;
    }

    public int getStrength() {
        return strength;
    }

    public String getShortName() {
        return shortName;
    }
}
