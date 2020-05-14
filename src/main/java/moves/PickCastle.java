package moves;

import model.GameState;
import model.cards.card.Card;

import java.util.List;

public class PickCastle extends CastleMove {

    public PickCastle(int player, List<Card> castle) {
        super(player);
        this.cards = castle;
    }

    @Override
    public void doMove(GameState gameState) {
        gameState.getPlayerModels().get(player).getFaceUpCastleCards().addCards(cards);
        gameState.getPlayerModels().get(player).getHand().removeCards(cards);
        gameState.getPlayerModels().get(player).setHasPickedCastle(true);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards) {
            stringBuilder.append(card.toShortString()).append(", ");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length() - 1, "");
        return "Player" + (player + 1) + " picked " + stringBuilder + "as their CASTLE.";
    }

    @Override
    public String toHumanString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards) {
            stringBuilder.append(card.toShortString()).append(", ");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length() - 1, "");
        return "Pick " + stringBuilder + "as your Castle.";
    }
}
