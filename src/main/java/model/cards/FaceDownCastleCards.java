package model.cards;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.cards.card.Card;

public class FaceDownCastleCards extends CastleCards {

    public FaceDownCastleCards() {
        super();
    }

    private FaceDownCastleCards(ObservableList<Card> cards) {
        super(cards);
    }

    @Override
    public void printOptions(boolean offerPickup, boolean hidden) {
        System.out.println("Face down model.cards: ");
        super.printOptions(offerPickup, hidden);
    }

    public FaceDownCastleCards copy() {
        ObservableList<Card> newCollection = FXCollections.observableArrayList(getCardCollection());
        return new FaceDownCastleCards(newCollection);
    }
}
