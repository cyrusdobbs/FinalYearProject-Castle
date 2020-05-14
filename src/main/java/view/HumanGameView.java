package view;

import model.GameState;
import model.PlayerModel;
import util.CastleConstants;
import util.terminal.InputConstants;
import util.terminal.TerminalUtil;

import java.util.ArrayList;

import static util.terminal.TerminalUtil.println;

public class HumanGameView implements GameView {

    @Override
    public void updateView(GameState gameState) {
        println(gameState.getLastMove().toString());

        if (gameState.getLastMove().burnsPile()) {
            return;
        }

        if (gameState.getDiscardPile().getTopCard() != null) {
            println("Top card: " + gameState.getDiscardPile().getTopCard().toShortString());
        } else {
            println("Top card: None");
        }

        printTable(gameState);

        if (gameState.getWinningPlayer() != -1) {
            println(gameState.getPlayerModels().get(gameState.getWinningPlayer()).getName() + " wins the game.");

            println("Play again? ");
            println("1. Yes");
            println("2. No");
            int input = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_TWO_REGEX);
            if (input == 2) {
                System.exit(0);
            }
        }
    }

    @Override
    public void startView(GameState gameState) {
        printTable(gameState);
    }

    private void printTable(GameState gameState) {
        Table tableOfPlayers = new Table(new ArrayList<>());
        tableOfPlayers.addCol("Name", "Hand", "FUTC", "FDTC");
        for (PlayerModel player : gameState.getPlayerModels()) {
            if (player.getName().equals(CastleConstants.HUMAN)) {
                tableOfPlayers.addHumanPlayer(player);
            } else {
                tableOfPlayers.addHiddenPlayer(player);
            }
        }
        println(tableOfPlayers.toString(2));
    }
}
