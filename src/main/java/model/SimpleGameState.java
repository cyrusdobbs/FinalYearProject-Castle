package model;

import model.cards.FaceUpCastleCards;
import model.cards.Hand;
import model.cards.card.Card;

public class SimpleGameState {

    private Hand hand;
    private FaceUpCastleCards castleFU;
    private int castleFDSize;
    private int opHandSize;
    private FaceUpCastleCards opCastleFU;
    private int opCastleFDSize;
    private Card topCard;
    private boolean deckEmpty;

    public SimpleGameState(Hand hand, FaceUpCastleCards castleFU, int castleFDSize, int opHandSize, FaceUpCastleCards opCastleFU, int opCastleFDSize, Card topCard, boolean deckEmpty) {
        this.hand = hand;
        this.castleFU = castleFU;
        this.castleFDSize = castleFDSize;
        this.opHandSize = opHandSize;
        this.opCastleFU = opCastleFU;
        this.opCastleFDSize = opCastleFDSize;
        this.topCard = topCard;
        this.deckEmpty = deckEmpty;
    }

    public Hand getHand() {
        return hand;
    }

    public FaceUpCastleCards getCastleFU() {
        return castleFU;
    }

    public int getCastleFDSize() {
        return castleFDSize;
    }

    public int getOpHandSize() {
        return opHandSize;
    }

    public FaceUpCastleCards getOpCastleFU() {
        return opCastleFU;
    }

    public int getOpCastleFDSize() {
        return opCastleFDSize;
    }

    public Card getTopCard() {
        return topCard;
    }

    public boolean isDeckEmpty() {
        return deckEmpty;
    }
}
