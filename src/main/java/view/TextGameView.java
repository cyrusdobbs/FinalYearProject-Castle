package view;

import model.GameState;
import model.PlayerModel;

import java.util.ArrayList;

public class TextGameView implements GameView {

    public void updateView(GameState gameState) {
        System.out.println(gameState.getLastMove().toString());
        System.out.println("Current player: " + gameState.getPlayerModels().get(gameState.getCurrentPlayer()).getName());
        System.out.println("Deck preview:");
        System.out.println(gameState.getDeck().toString());
        if (gameState.getDiscardPile().getTopCard() != null) {
            System.out.println("Top card: " + gameState.getDiscardPile().getTopCard().toShortString());
        }
        System.out.println();
        System.out.println("Players:");
        Table tableOfPlayers = new Table(new ArrayList<>());
        tableOfPlayers.addCol("Name", "Hand", "FUTC", "FDTC");
        for (PlayerModel player : gameState.getPlayerModels()) {
            tableOfPlayers.addPlayer(player);
        }
        System.out.println(tableOfPlayers.toString(2));
        System.out.println();

        if (gameState.getWinningPlayer() != -1) {
            System.out.println(gameState.getPlayerModels().get(gameState.getWinningPlayer()).getName() + " WON the game.");
        }
    }

    @Override
    public void startView(GameState gameState) {
    }


}
