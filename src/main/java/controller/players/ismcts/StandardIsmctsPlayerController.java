package controller.players.ismcts;

import model.GameState;
import model.Player;

public class StandardIsmctsPlayerController extends IsmctsPlayerController {

    public StandardIsmctsPlayerController(Player playerModel, int playerNo, int maxIterations, boolean verbose, boolean print) {
        super(playerModel, playerNo, maxIterations, verbose, print);
    }

    @Override
    protected void simulate(GameState currentGameState) {
        while (!getMoves(currentGameState).isEmpty()) {
            doMove(getRandomMove(getMoves(currentGameState)), currentGameState);
        }
    }

    @Override
    protected int getWinningPlayer(GameState currentGameState) {
        return currentGameState.getWinningPlayer();
    }
}
