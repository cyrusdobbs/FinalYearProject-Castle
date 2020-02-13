package controller;

import controller.players.IsmctsPlayerController;
import controller.players.LowestPlayerController;
import controller.players.PlayerController;
import moves.*;
import model.Player;
import model.GameState;
import view.GameView;
import view.TextGameView;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GameState gameState;
    private GameView gameView;

    private List<PlayerController> players;

    public GameController(GameState gameState, GameView gameView, List<PlayerController> players) {
        this.gameState = gameState;
        this.gameView = gameView;
        this.players = players;
    }

    public static void main(String[] args) {
        List<Player> playerModels = new ArrayList<>();
        playerModels.add(new Player("ISMCTS"));
        playerModels.add(new Player("Lowest1"));
//        playerModels.add(new Player("Lowest2"));
        List<PlayerController> players = new ArrayList<>();
        players.add(new IsmctsPlayerController(playerModels.get(0), 2500, false, true));
        players.add(new LowestPlayerController(playerModels.get(1)));
//        players.add(new LowestPlayerController(playerModels.get(2)));

        GameController game = new GameController(new GameState(playerModels), new TextGameView(), players);
        game.run();
    }

    private void run() {
        boolean print = true;
        boolean gameOver = false;
        do {
            for (PlayerController player : players) {
                if (gameOver) {
                    break;
                }
                boolean playersTurn = true;
                while (playersTurn) {

                    CastleMove move = player.getMove(gameState);
                    doMove(move);

                    if (print && gameView != null) {
                        gameView.updateView(gameState);
                    }

                    // If the player does not burn the pile then it is the next players turn
                    if (!move.burnsPile()) {
                        playersTurn = false;
                    }
                    if (gameState.isGameOver()) {
                        gameOver = true;
                        break;
                    }
                }
            }
        } while (!gameOver);
    }

    // Needed
    public void doMove(CastleMove move) {
        move.doMove(gameState);
        // If a player burns the pile then they have another go
        if (!move.burnsPile() && !gameState.isGameOver()) {
            gameState.setCurrentPlayer(gameState.getNextPlayer());
        }
        gameState.setLastMove(move);
    }
}
