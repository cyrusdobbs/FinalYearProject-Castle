package controller.players.ismcts;

import model.GameState;
import model.Player;
import util.Evaluator;

public class EvaluatingIsmctsPlayerController extends IsmctsPlayerController {

    Evaluator evaluator;
    int maxDepth;

    public EvaluatingIsmctsPlayerController(Player playerModel, int maxIterations, boolean verbose, boolean print, Evaluator evaluator, int maxDepth) {
        super(playerModel, maxIterations, verbose, print);
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }

    @Override
    protected void simulate(GameState currentGameState) {
        int depth = 0;
        while (depth < maxDepth & !getMoves(currentGameState).isEmpty()) {
            doMove(getRandomMove(getMoves(currentGameState)), currentGameState);
            depth++;
        }
    }

    @Override
    protected int getWinningPlayer(GameState currentGameState) {
        // If the game is unfinished, predict the winner.
        int winningPlayer = currentGameState.getWinningPlayer();
        if (winningPlayer == -1) {
            currentGameState.isStateValid();
            int evaluation = evaluator.evaluate(currentGameState.toSimpleGameState());
            boolean mctsToWin = evaluation == 1;
            winningPlayer = mctsToWin ? 1 : 0;
        }
        return winningPlayer;
    }
}
