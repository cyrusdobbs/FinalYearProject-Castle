package moves;

import model.GameState;
import model.cards.card.Card;

import java.util.ArrayList;

public class PlayFaceDownCastleCard extends PlayCard {

    private int cardPickedIndex;
    private boolean triedToPlay = false;
    private String pickedUpString;

    public PlayFaceDownCastleCard(int player, int cardPickedIndex) {
        super(player, new ArrayList<>());
        this.cardPickedIndex = cardPickedIndex;
    }

    @Override
    public void doMove(GameState gameState) {
        // If card is valid it is played
        Card cardPicked = gameState.getPlayerModels().get(player).getFaceDownCastleCards().getCardCollection().get(cardPickedIndex);
        cards.add(cardPicked);
        if (cardPicked.isCardValid(gameState.getDiscardPile().getTopCard())) {
            gameState.getPlayerModels().get(player).getFaceDownCastleCards().removeCards(cards);
            super.doMove(gameState);
        }
        // Otherwise pick up pile
        else {
            gameState.getPlayerModels().get(player).getFaceDownCastleCards().removeCards(cards);
            gameState.getPlayerModels().get(player).getHand().addCards(cards);
            PickUp pickUp = new PickUp(player);
            pickUp.doMove(gameState);
            triedToPlay = true;
            pickedUpString = pickUp.toString();
        }
    }

    @Override
    public String toString() {
        if (triedToPlay) {
            return "Player" + (player + 1) + " tried to play " + cards.get(0).toShortString() + " from FDCastle." + "\n" + pickedUpString;
        }
        return super.toString() + "FDCastle" + (burnsPile ? " and BURNED the pile." : ".");
    }

    @Override
    public String toHumanString() {
        return super.toHumanString() + "FDCastle.";
    }
}
