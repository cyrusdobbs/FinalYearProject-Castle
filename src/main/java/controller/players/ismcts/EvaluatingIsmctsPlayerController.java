package controller.players.ismcts;

import model.GameState;
import model.Player;
import org.nd4j.linalg.api.ndarray.INDArray;
import util.CastleConstants;
import util.Evaluator;
import util.Run;

public class EvaluatingIsmctsPlayerController extends IsmctsPlayerController {

    Evaluator evaluator;
    int maxDepth;

    public EvaluatingIsmctsPlayerController(Player playerModel, int playerNo, int maxIterations, boolean verbose, boolean print, Evaluator evaluator, int maxDepth) {
        super(playerModel, playerNo, maxIterations, verbose, print);
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }

    @Override
    protected void simulate(GameState currentGameState) {
        int depth = 0;
        // While below maxDepth or
        while ((depth < maxDepth || currentGameState.getCurrentPlayer() != playerNo) & !getMoves(currentGameState).isEmpty()) {
            doMove(getRandomMove(getMoves(currentGameState)), currentGameState);
            depth++;
        }
    }

    @Override
    protected int getWinningPlayer(GameState currentGameState) {
        // If the game is unfinished, predict the winner.
        int winningPlayer = currentGameState.getWinningPlayer();
        if (winningPlayer == -1) {
            boolean thisPlayerToWin = evaluator.evaluate(currentGameState.toNDArray(playerNo)) == 1;
            winningPlayer = thisPlayerToWin ? playerNo : 1 - playerNo;
        }

        INDArray array = currentGameState.toNDArray(playerNo);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            evaluator.evaluate(array);
        }
        System.out.println((System.currentTimeMillis() - startTime) / 1000);
        System.exit(0);
        return winningPlayer;
    }
}
