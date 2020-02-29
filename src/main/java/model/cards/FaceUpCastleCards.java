package model.cards;

import model.cards.card.Card;

import java.util.ArrayList;
import java.util.List;

public class FaceUpCastleCards extends CastleCards {

    public FaceUpCastleCards() {
        super();
    }

    private FaceUpCastleCards(List<Card> cards) {
        super(cards);
    }

    @Override
    public void printOptions(boolean offerPickup, boolean hidden) {
        System.out.println("Face up model.cards: ");
        super.printOptions(offerPickup, hidden);
    }

    public FaceUpCastleCards copy() {
        List<Card> newCollection = new ArrayList<>(getCardCollection());
        return new FaceUpCastleCards(newCollection);
    }
}
