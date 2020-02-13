package model.cards;

import model.cards.card.Suit;

import java.util.*;

public class CardUtil {

    public static Set<Suit> getSuits() {
        return new HashSet<>(EnumSet.allOf(Suit.class));
    }
}
