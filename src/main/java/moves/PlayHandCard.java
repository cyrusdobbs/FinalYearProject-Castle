package moves;

import model.GameState;
import model.cards.card.Card;

import java.util.List;

public class PlayHandCard extends PlayCard {

    private String topDeckString = "";

    public PlayHandCard(int player, List<Card> cardsPlayed) {
        super(player, cardsPlayed);
    }

    @Override
    public void doMove(GameState gameState) {
        // Remove card from hand
        gameState.getPlayerModels().get(player).getHand().removeCards(cards);
        // Play card
        super.doMove(gameState);
        // Take top deck
        if (!gameState.getDeck().isEmpty() && gameState.getPlayerModels().get(player).getHand().size() < gameState.getHandSize()) {
            pickUpTopDeck(gameState);
        }
    }

    private void pickUpTopDeck(GameState gameState) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" and TOP DECKED ");
        while (!gameState.getDeck().isEmpty() && gameState.getPlayerModels().get(player).getHand().size() < gameState.getHandSize()) {
            Card topDeckCard = gameState.getDeck().topDeck();
            gameState.getPlayerModels().get(player).getHand().addCard(topDeckCard);
            stringBuilder.append(topDeckCard.toShortString()).append(", ");
        }
        topDeckString = stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString();
    }

    @Override
    public String toString() {
        return super.toString() + "HAND" + topDeckString + (burnsPile ? " and BURNED the pile." : ".");
    }

    @Override
    public String toHumanString() {
        return super.toHumanString() + "hand.";
    }
}
