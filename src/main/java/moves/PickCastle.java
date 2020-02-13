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
        gameState.getPlayers().get(player).getFaceUpCastleCards().addCards(cards);
        gameState.getPlayers().get(player).getHand().removeCards(cards);
        gameState.getPlayers().get(player).setHasPickedCastle(true);
    }

    @Override
    public String toString() {
        return "Player" + player + " picked " + cards.toString() + " as their CASTLE.";
    }
}
