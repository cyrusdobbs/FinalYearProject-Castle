package controller.players.ismcts;

import controller.players.PlayerController;
import model.GameState;
import model.PlayerModel;
import org.nd4j.linalg.factory.Nd4j;
import util.Run;

public class EvaluatingIsmctsPlayerController extends IsmctsPlayerController {

    Evaluator evaluator;
    int maxDepth;

    public EvaluatingIsmctsPlayerController(PlayerModel playerModel, int playerNo, int maxIterations, boolean verbose, boolean print, Evaluator evaluator, int maxDepth) {
        super(playerModel, playerNo, maxIterations, verbose, print);
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }

    @Override
    protected void simulate(GameState currentGameState) {
        int depth = 0;
        // If we are below max depth and the current player is 'this' then we stop the simulation (or if a terminal state is reach first)
        while ((depth < maxDepth || currentGameState.getCurrentPlayer() != playerNo) & !getMoves(currentGameState).isEmpty()) {
            doMove(getRandomMove(getMoves(currentGameState)), currentGameState);
            depth++;
        }
    }

    @Override
    protected int getWinningPlayer(GameState currentGameState) {
        long startTime = System.currentTimeMillis();

        // If the game is unfinished, predict the winner.
        int winningPlayer = currentGameState.getWinningPlayer();
        if (winningPlayer == -1) {
            Nd4j.getWorkspaceManager().setWorkspaceForCurrentThread(Nd4j.getWorkspaceManager().createNewWorkspace());
            boolean thisPlayerToWin = evaluator.evaluate(currentGameState.toNDArray(playerNo));
            winningPlayer = thisPlayerToWin ? playerNo : 1 - playerNo;
            Nd4j.getWorkspaceManager().getAllWorkspacesForCurrentThread().get(0).destroyWorkspace();
        }

        Run.evalTime += (System.currentTimeMillis() - startTime);
        return winningPlayer;
    }

    @Override
    public PlayerController copy() {
        return new EvaluatingIsmctsPlayerController(playerModel.copy(), playerNo, maxIterations, verbose, print, evaluator, maxDepth);
    }
}
