package controller.players;


import model.GameState;
import model.PlayerModel;
import moves.CastleMove;

import java.util.List;

public class RandomPlayer extends PlayerController {

    public RandomPlayer(PlayerModel playerModel, int playerNo) {
        super(playerModel, playerNo);
    }

    @Override
    public CastleMove getMove(GameState gameState) {
        List<CastleMove> moves = getMoves(gameState);

        return getRandomMove(moves);
    }

    @Override
    public PlayerController copy() {
        return new RandomPlayer(playerModel.copy(), playerNo);
    }
}
