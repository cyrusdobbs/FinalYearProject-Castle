package model.cards;

import model.cards.card.Card;

import java.util.ArrayList;
import java.util.List;

public class FaceDownCastleCards extends CastleCards {

    public FaceDownCastleCards() {
        super();
    }

    private FaceDownCastleCards(List<Card> cards) {
        super(cards);
    }

    @Override
    public void printOptions(boolean offerPickup, boolean hidden) {
        System.out.println("Face down model.cards: ");
        super.printOptions(offerPickup, hidden);
    }

    public FaceDownCastleCards copy() {
        List<Card> newCollection = new ArrayList<>(getCardCollection());
        return new FaceDownCastleCards(newCollection);
    }
}
