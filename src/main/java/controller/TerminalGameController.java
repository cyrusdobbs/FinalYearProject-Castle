package controller;

import Util.Exporter;
import controller.players.AIController;
import controller.players.IsmctsPlayerController;
import controller.players.LowestPlayerController;
import model.Game;
import model.SimpleGameState;
import moves.*;
import model.Player;
import model.GameState;
import view.TextGameView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TerminalGameController {

    private static final int MINUTE_TO_MS = 60000;
    private static final String TIME = "T";
    private static final String GAMES = "G";
    private static final String YES = "Y";

    private GameState gameState;
    private TextGameView gameView;

    private List<AIController> players;
    private Exporter exporter = new Exporter("GameStates");

    private TerminalGameController(GameState gameState, TextGameView gameView, List<AIController> players) throws IOException {
        this.gameState = gameState;
        this.gameView = gameView;
        this.players = players;
    }

    public static void main(String[] args) throws IOException {
        List<Player> playerModels = new ArrayList<>();
        playerModels.add(new Player("ISMCTS"));
        playerModels.add(new Player("Lowest"));

        List<AIController> players = new ArrayList<>();
        players.add(new IsmctsPlayerController(playerModels.get(0), 2500, false, true));
        players.add(new LowestPlayerController(playerModels.get(1)));

        TerminalGameController game = new TerminalGameController(new GameState(playerModels), new TextGameView(), players);
        game.run(args[0], Integer.parseInt(args[1]), args[2].equals(YES), args[3].equals(YES));
    }

    private void run(String endCondition, int amount, boolean print, boolean exportToCSV) throws IOException {
        if (endCondition.equals(TIME)) {

            long endTime = System.currentTimeMillis() + (amount * MINUTE_TO_MS);
            while (System.currentTimeMillis() < endTime) {

                Game game = runGame(print);
                if (exportToCSV) {
                    exporter.exportGame(game);
                }

            }
        } else if (endCondition.equals(GAMES)) {

            for (int i = 0; i < amount; i++) {

                Game game = runGame(print);
                if (exportToCSV) {
                    exporter.exportGame(game);
                }
            }
        }
        exporter.close();
    }

    private Game runGame(boolean print) {
        List<SimpleGameState> gameStates = new ArrayList<>();
        do {

            for (AIController player : players) {
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

                    if (gameState.getCurrentPlayer() == ControllerConstants.AI_PLAYER) {
                        gameStates.add(gameState.toSimpleGameState());
                    }

                    if (gameState.isGameOver()) {
                        return new Game(gameStates, gameState.getCurrentPlayer() == 0);
                    }
                }
            }
        } while (true);
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
