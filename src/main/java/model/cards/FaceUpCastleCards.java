package model.cards;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.cards.card.Card;


public class FaceUpCastleCards extends CastleCards {

    public FaceUpCastleCards() {
        super();
    }

    private FaceUpCastleCards(ObservableList<Card> cards) {
        super(cards);
    }

    @Override
    public void printOptions(boolean offerPickup, boolean hidden) {
        System.out.println("Face up model.cards: ");
        super.printOptions(offerPickup, hidden);
    }

    public FaceUpCastleCards copy() {
        ObservableList<Card> newCollection = FXCollections.observableArrayList(getCardCollection());
        return new FaceUpCastleCards(newCollection);
    }
}
