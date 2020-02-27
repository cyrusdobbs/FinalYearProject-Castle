package model;

import model.cards.FaceUpCastleCards;
import model.cards.Hand;
import model.cards.card.Card;

public class SimpleGameState {

    private Hand hand;
    private FaceUpCastleCards castle;
    private FaceUpCastleCards opCastle;
    private Card faceUpCard;

    public SimpleGameState(Hand hand, FaceUpCastleCards castle, FaceUpCastleCards opCastle, Card faceUpCard) {
        this.hand = hand;
        this.castle = castle;
        this.opCastle = opCastle;
        this.faceUpCard = faceUpCard;
    }

    public Hand getHand() {
        return hand;
    }

    public FaceUpCastleCards getCastle() {
        return castle;
    }

    public FaceUpCastleCards getOpCastle() {
        return opCastle;
    }

    public Card getFaceUpCard() {
        return faceUpCard;
    }
}
