package model.cards;

import model.cards.card.Card;

import java.util.List;

abstract class CastleCards extends CardCollection {

    protected CastleCards() {
        super();
    }

    public CastleCards(List<Card> cards) {
        super(cards);
    }
}
