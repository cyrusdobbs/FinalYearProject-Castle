package controller.players.ismcts;

import controller.players.PlayerController;
import model.GameState;
import model.PlayerModel;

public class StandardIsmctsPlayerController extends IsmctsPlayerController {

    public StandardIsmctsPlayerController(PlayerModel playerModel, int playerNo, int maxIterations, boolean verbose, boolean print) {
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

    @Override
    public PlayerController copy() {
        return new StandardIsmctsPlayerController(playerModel.copy(), playerNo, maxIterations, verbose, print);
    }
}
