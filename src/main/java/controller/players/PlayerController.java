package controller.players;

import model.Player;

public abstract class PlayerController {

    protected int playerNo;
    Player playerModel;

    public PlayerController(Player playerModel, int playerNo) {
        this.playerModel = playerModel;
        this.playerNo = playerNo;
    }
}
