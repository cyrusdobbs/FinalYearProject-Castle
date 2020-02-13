package model.cards.card;

import javafx.scene.Parent;

public class Card extends Parent {

    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(Card card) {
        this.rank = card.rank;
        this.suit = card.suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    public String toShortString() {
        return rank.getShortName() + suit.getShortName();
    }

    public boolean isCardValid(Card topCard) {
        return topCard == null || isHigherThanTopCard(topCard) || isSpecial();
    }

    private boolean isHigherThanTopCard(Card topCard) {
        return topCard.getRank().getValueCode() <= this.getRank().getValueCode();
    }

    private boolean isSpecial() {
        return this.rank.getStrength() > 10;
    }
}
