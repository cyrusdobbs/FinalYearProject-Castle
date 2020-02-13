package model;

import moves.*;
import model.cards.Deck;
import model.cards.DiscardPile;

import java.util.*;

public class GameState {

    private Deck deck;
    private DiscardPile discardPile;
    private List<Player> players;
    private int currentPlayer;
    private int handSize;
    private int castleSize;
    private int noOfPlayers;
    private boolean gameOver;
    private CastleMove lastMove;
    private int winningPlayer;

    public GameState(List<Player> playerModels) {
        deck = new Deck();
        deck.shuffle();
        discardPile = new DiscardPile();
        deck.shuffle();

        players = new ArrayList<>();
        players.addAll(playerModels);

        currentPlayer = 0;
        handSize = 3;
        castleSize = 3;
        noOfPlayers = playerModels.size();
        gameOver = false;
        lastMove = null;
        winningPlayer = -1;

        // Deal castle
        for (int x = 0; x < castleSize; x++) {
            for (Player player : players) {
                player.getFaceDownCastleCards().addCard(deck.topDeck());
            }
        }
        // Deal hands
        for (int x = 0; x < handSize + castleSize; x++) {
            for (Player player : players) {
                player.getHand().addCard(deck.topDeck());
            }
        }
    }
    public GameState(Deck deck, DiscardPile discardPile, List<Player> players, int handSize, int castleSize) {
        this.deck = deck;
        this.discardPile = discardPile;
        this.players = players;
        this.handSize = handSize;
        this.castleSize = castleSize;
        noOfPlayers = players.size();
        gameOver = false;
        currentPlayer = 0;
        lastMove = null;
    }

    public GameState(Deck deck,
                     DiscardPile discardPile,
                     List<Player> players,
                     int currentPlayer,
                     int handSize,
                     int castleSize,
                     int noOfPlayers,
                     boolean gameOver,
                     CastleMove lastMove) {
        this.deck = deck;
        this.discardPile = discardPile;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.handSize = handSize;
        this.castleSize = castleSize;
        this.noOfPlayers = noOfPlayers;
        this.gameOver = gameOver;
        this.lastMove = lastMove;
    }

    public int getResult(int playerJustMoved) {
        return (gameOver && playerJustMoved == winningPlayer) ? 1 : 0;
    }

    public Deck getDeck() {
        return deck;
    }

    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    public List<Player> getPlayers() {
        return players;
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

    public GameState copy() {
        return new GameState(new Deck(deck), new DiscardPile(discardPile), copyPlayers(), currentPlayer, handSize, castleSize, noOfPlayers, gameOver, lastMove);
    }

    private List<Player> copyPlayers() {
        List<Player> playersCopy = new ArrayList<>();
        for (Player player : players) {
            playersCopy.add(player.copy());
        }
        return playersCopy;
    }

    public int getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(int winningPlayer) {
        this.winningPlayer = winningPlayer;
    }
}
