package model.cards;

import model.cards.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand extends CardCollection {

    public Hand() {
    }

    public Hand(List<Card> cards) {
        super(cards);
    }

    @Override
    public void printOptions(boolean offerPickup, boolean hidden) {
        System.out.println("Hand: ");
        super.printOptions(offerPickup, hidden);
    }

    public Hand copy() {
        List<Card> newCollection = new ArrayList<>(getCardCollection());
        return new Hand(newCollection);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
