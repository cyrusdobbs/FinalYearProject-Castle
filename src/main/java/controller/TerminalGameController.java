package controller;

import controller.players.PlayerController;
import model.GameHistory;
import model.SimpleGameState;
import moves.*;
import model.GameState;
import view.GameView;

import java.util.ArrayList;
import java.util.List;

public class TerminalGameController {

    private GameState gameState;
    private GameView gameView;

    private List<PlayerController> players;
    private int trackedPlayer;

    public TerminalGameController(GameState gameState, GameView gameView, List<PlayerController> players, int trackedPlayer) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.players = players;
        this.trackedPlayer = trackedPlayer;
    }

    public GameHistory run(boolean print) {
        List<SimpleGameState> gameStates = new ArrayList<>();
        gameView.startView(gameState);
        do {

            for (PlayerController player : players) {
                while (true) {

                    // Snapshot GameState at the beginning of every AI turn
                    if (gameState.getCurrentPlayer() == trackedPlayer) {
                        gameStates.add(gameState.toSimpleGameState(trackedPlayer));
                    }

                    CastleMove move = player.getMove(gameState);
                    doMove(move);

                    if (print && gameView != null) {
                        gameView.updateView(gameState);
                    }

                    if (gameState.isGameOver()) {
                        return new GameHistory(gameStates, gameState.getWinningPlayer() == trackedPlayer);
                    }

                    // If the player does not burn the pile then it is the next players turn
                    if (!move.burnsPile()) {
                        endTurn(move);
                        break;
                    }
                }
            }
        } while (true);
    }

    // TODO: I don't like this
    // IF THIS CHANGES CHANGE IN ISMCTS CONTROLLER
    public void doMove(CastleMove move) {
        move.doMove(gameState);
        gameState.setLastMove(move);
    }

    private void endTurn(CastleMove move) {
        // If a player burns the pile then they have another go
        if (!move.burnsPile() && !gameState.isGameOver()) {
            gameState.setCurrentPlayer(gameState.getNextPlayer());
        }
    }
}
