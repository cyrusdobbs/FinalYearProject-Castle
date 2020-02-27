package controller.players;

import model.Player;

public abstract class PlayerController {

    Player playerModel;

    public PlayerController(Player playerModel) {
        this.playerModel = playerModel;
    }
}
