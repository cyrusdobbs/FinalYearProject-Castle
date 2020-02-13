package model;

import model.cards.FaceDownCastleCards;
import model.cards.FaceUpCastleCards;
import model.cards.Hand;

public class Player {

    private Hand hand;
    private FaceUpCastleCards faceUpCastleCards;
    private FaceDownCastleCards faceDownCastleCards;
    private String name;
    private boolean hasPickedCastle;

    public Player(String name) {
        hand = new Hand();
        faceUpCastleCards = new FaceUpCastleCards();
        faceDownCastleCards = new FaceDownCastleCards();
        hasPickedCastle = false;
        this.name = name;
    }

    protected Player(Hand hand, FaceUpCastleCards faceUpCastleCards, FaceDownCastleCards faceDownCastleCards, String name, boolean hasPickedCastle) {
        this.hand = hand;
        this.faceUpCastleCards = faceUpCastleCards;
        this.faceDownCastleCards = faceDownCastleCards;
        this.name = name;
        this.hasPickedCastle = hasPickedCastle;
    }

    public Hand getHand() {
        return hand;
    }

    public FaceUpCastleCards getFaceUpCastleCards() {
        return faceUpCastleCards;
    }

    public FaceDownCastleCards getFaceDownCastleCards() {
        return faceDownCastleCards;
    }

    public String getName() {
        return name;
    }

    public boolean hasPickedCastle() {
        return hasPickedCastle;
    }

    public void setHasPickedCastle(boolean hasPickedCastle) {
        this.hasPickedCastle = hasPickedCastle;
    }

    public Player copy() {
        return new Player(getHand().copy(), getFaceUpCastleCards().copy(), getFaceDownCastleCards().copy(), getName(), hasPickedCastle());
    }
}
