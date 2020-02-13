package controller.players;

import actions.CastleMove;
import model.GameState;
import model.Player;
import view.players.HumanPlayerView;

public class HumanPlayerController extends PlayerController {

    private HumanPlayerView playerView;

    public HumanPlayerController(Player playerModel, HumanPlayerView playerView) {
        super(playerModel);
        this.playerView = playerView;
    }

    @Override
    public CastleMove getMove(GameState gameState) {
        return null;
    }
}
