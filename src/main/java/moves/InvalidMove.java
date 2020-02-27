package moves;

import model.GameState;

public class InvalidMove extends CastleMove {

    private String message;

    public InvalidMove(int player, String message) {
        super(player);
        this.message = message;
    }

    @Override
    public void doMove(GameState gameState) {
    }

    @Override
    public String toString() {
        return null;
    }

    public String getMessage() {
        return message;
    }
}
