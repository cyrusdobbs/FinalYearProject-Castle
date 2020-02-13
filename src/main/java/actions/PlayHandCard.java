package actions;

import model.GameState;
import model.cards.card.Card;

import java.util.List;

public class PlayHandCard extends PlayCard {

    private String topDeckString = "";

    public PlayHandCard(int player, Card cardPlayed) {
        super(player, cardPlayed);
    }

    public PlayHandCard(int player, List<Card> cardsPlayed) {
        super(player, cardsPlayed);
    }

    @Override
    public void doAction(GameState gameState) {
        // Remove model.cards.card from hand
        gameState.getPlayers().get(player).getHand().removeCards(cards);
        // Play model.cards.card
        super.doAction(gameState);
        // Take top deck
        if (!gameState.getDeck().isEmpty() && gameState.getPlayers().get(player).getHand().size() < gameState.getHandSize()) {
            pickUpTopDeck(gameState);
        }
    }

    private void pickUpTopDeck(GameState gameState) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" and TOP DECKED ");
        while (!gameState.getDeck().isEmpty() && gameState.getPlayers().get(player).getHand().size() < gameState.getHandSize()) {
            Card topDeckCard = gameState.getDeck().topDeck();
            gameState.getPlayers().get(player).getHand().addCard(topDeckCard);
            stringBuilder.append(topDeckCard.toShortString()).append(", ");
        }
        topDeckString = stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString();
    }

    @Override
    public String toString() {
        return super.toString() + "HAND" + topDeckString + (burnsPile ? " and BURNED the pile." : ".");
    }
}
