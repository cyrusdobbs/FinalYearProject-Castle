package model;

import model.cards.card.Card;
import moves.*;
import model.cards.Deck;
import model.cards.DiscardPile;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.*;

public class GameState {

    private Deck deck;
    private DiscardPile discardPile;
    private List<Card> burnedCards;
    private List<PlayerModel> playerModels;
    private int currentPlayer;
    private int handSize;
    private int castleSize;
    private int noOfPlayers;
    private boolean gameOver;
    private CastleMove lastMove;
    private int winningPlayer;

    public GameState(List<PlayerModel> playerModels) {
        deck = new Deck();
        deck.shuffle();
        discardPile = new DiscardPile();
        burnedCards = new ArrayList<>();
        deck.shuffle();

        this.playerModels = new ArrayList<>();
        this.playerModels.addAll(playerModels);

        currentPlayer = 0;
        handSize = 3;
        castleSize = 3;
        noOfPlayers = playerModels.size();
        gameOver = false;
        lastMove = null;
        winningPlayer = -1;

        // Deal castle
        for (int x = 0; x < castleSize; x++) {
            for (PlayerModel player : this.playerModels) {
                player.getFaceDownCastleCards().addCard(deck.topDeck());
            }
        }
        // Deal hands
        for (int x = 0; x < handSize + castleSize; x++) {
            for (PlayerModel player : this.playerModels) {
                player.getHand().addCard(deck.topDeck());
            }
        }
    }

    public Deck getDeck() {
        return deck;
    }

    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    public List<Card> getBurnedCards() {
        return burnedCards;
    }

    public List<PlayerModel> getPlayerModels() {
        return playerModels;
    }

    public int getHandSize() {
        return handSize;
    }

    public int getCastleSize() {
        return castleSize;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getNextPlayer() {
        return (currentPlayer + 1) % noOfPlayers;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public CastleMove getLastMove() {
        return lastMove;
    }

    public void setLastMove(CastleMove lastMove) {
        this.lastMove = lastMove;
    }

    public int getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(int winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public SimpleGameState toSimpleGameState(int povPlayer) {
        int otherPlayer = 1 - povPlayer;
        return new SimpleGameState(playerModels.get(povPlayer).getHand().copy(),
                playerModels.get(povPlayer).getFaceUpCastleCards().copy(),
                playerModels.get(povPlayer).getFaceDownCastleCards().size(),
                playerModels.get(otherPlayer).getHand().size(),
                playerModels.get(otherPlayer).getFaceUpCastleCards().copy(),
                playerModels.get(otherPlayer).getFaceDownCastleCards().size(),
                discardPile.getTopCard(),
                deck.isEmpty());
    }

    public INDArray toNDArray(int povPlayer) {
        int otherPlayer = 1 - povPlayer;

        INDArray array = Nd4j.hstack(playerModels.get(povPlayer).getHand().toNDArray(), playerModels.get(povPlayer).getFaceUpCastleCards().toNDArray());
        array = Nd4j.hstack(array, playerModels.get(otherPlayer).getFaceUpCastleCards().toNDArray());

        INDArray zeros = Nd4j.zeros(new int[]{4, 2});
        array = Nd4j.hstack(array, zeros);

        for (int i = 0; i < playerModels.get(otherPlayer).getHand().size(); i++) {
            array.putScalar(0, 39, 1);
        }
        for (int i = 0; i < playerModels.get(otherPlayer).getFaceDownCastleCards().size(); i++) {
            array.putScalar(0, 40, 1);
        }

        return array;
    }

    public void isStateValid() {
        int[] counts = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        deck.updateCount(counts);
        discardPile.updateCounts(counts);

        if (!burnedCards.isEmpty()) {
            for (Card card : burnedCards) {
                counts[card.getRank().getValueCode() - 2]++;
            }
        }

        for (PlayerModel player : playerModels) {
            player.getHand().updateCount(counts);
            player.getFaceDownCastleCards().updateCount(counts);
            player.getFaceUpCastleCards().updateCount(counts);
        }

        for (int i = 0; i < 13; i++) {
            if (counts[i] != 4) {
                System.out.println("ERROR: Invalid state.");
                System.out.println(Arrays.toString(counts));
                System.exit(0);
                return;
            }
        }
    }

    public GameState copy() {
        return new GameState(new Deck(deck), new DiscardPile(discardPile), new ArrayList<>(burnedCards), copyPlayers(), currentPlayer, handSize, castleSize, noOfPlayers, gameOver, lastMove, winningPlayer);
    }

    private List<PlayerModel> copyPlayers() {
        List<PlayerModel> playersCopy = new ArrayList<>();
        for (PlayerModel player : playerModels) {
            playersCopy.add(player.copy());
        }
        return playersCopy;
    }

    public GameState(Deck deck,
                     DiscardPile discardPile,
                     List<Card> burnedCards,
                     List<PlayerModel> playerModels,
                     int currentPlayer,
                     int handSize,
                     int castleSize,
                     int noOfPlayers,
                     boolean gameOver,
                     CastleMove lastMove,
                     int winningPlayer) {
        this.deck = deck;
        this.discardPile = discardPile;
        this.burnedCards = burnedCards;
        this.playerModels = playerModels;
        this.currentPlayer = currentPlayer;
        this.handSize = handSize;
        this.castleSize = castleSize;
        this.noOfPlayers = noOfPlayers;
        this.gameOver = gameOver;
        this.lastMove = lastMove;
        this.winningPlayer = winningPlayer;
    }
}
