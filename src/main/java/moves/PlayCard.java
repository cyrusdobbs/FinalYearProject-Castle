package moves;

import model.GameState;
import model.cards.card.Card;

import java.util.Collections;
import java.util.List;

public abstract class PlayCard extends CastleMove {

    PlayCard(int player, Card cards) {
        super(player, Collections.singletonList(cards));
    }

    PlayCard(int player, List<Card> cards) {
        super(player, cards);
    }

    @Override
    public void doMove(GameState gameState) {
        // Play model.cards.card
        gameState.getDiscardPile().play(cards);
        isLastFourPlayedTheSame(gameState);
        isCardMagicTen(gameState);
        isGameOver(gameState);
    }

    private void isLastFourPlayedTheSame(GameState gameState) {
        if (gameState.getDiscardPile().isSameTopFourCards()) {
            burn(gameState);
        }
    }

    private void isCardMagicTen(GameState gameState) {
        if (cards.get(0).getRank().getValueCode() == 10) {
            burn(gameState);
        }
    }

    private void burn(GameState gameState) {
        gameState.getDiscardPile().burn();
        burnsPile = true;
    }

    private void isGameOver(GameState gameState) {
        if (gameState.getPlayers().get(player).getHand().getCardCollection().isEmpty() && gameState.getPlayers().get(player).getFaceDownCastleCards().isEmpty()) {
            gameState.setGameOver(true);
            gameState.setWinningPlayer(gameState.getCurrentPlayer());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards) {
            stringBuilder.append(card.toShortString()).append(", ");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length() - 1, "");
        return "Player" + player + " PLAYED " + stringBuilder +  "from ";
    }
}
