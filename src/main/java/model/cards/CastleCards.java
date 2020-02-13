package model.cards;


import javafx.collections.ObservableList;
import model.cards.card.Card;

abstract class CastleCards extends CardCollection {

    protected CastleCards() {
        super();
    }

    public CastleCards(ObservableList<Card> cards) {
        super(cards);
    }
}
