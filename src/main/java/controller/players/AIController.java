package controller.players;

import model.GameState;
import moves.CastleMove;

public interface AIController {

    CastleMove getMove(GameState gameState);
}
