package moves;

import model.GameState;

public class PickUp extends CastleMove {

    public PickUp(int player) {
        super(player);
    }

    @Override
    public void doMove(GameState gameState) {
        // Pick up discard pile
        gameState.getPlayerModels().get(player).getHand().addCards(gameState.getDiscardPile().pickUp());
    }

    @Override
    public String toString() {
        return "Player" + (player + 1) + " could not play and PICKED UP the pile.";
    }

    @Override
    public String toHumanString() {
        return "You cannot play anything. Pick up the pile.";
    }
}
