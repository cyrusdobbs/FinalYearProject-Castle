package model;

import model.cards.card.Card;
import moves.*;
import model.cards.Deck;
import model.cards.DiscardPile;

import java.util.*;

public class GameState {

    private Deck deck;
    private DiscardPile discardPile;
    private List<Card> burnedCards;
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
        burnedCards = new ArrayList<>();
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

    public Deck getDeck() {
        return deck;
    }

    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    public List<Card> getBurnedCards() {
        return burnedCards;
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

    public int getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(int winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public SimpleGameState toSimpleGameState(int povPlayer) {
        int otherPlayer = 1 - povPlayer;
        return new SimpleGameState(players.get(povPlayer).getHand().copy(),
                players.get(povPlayer).getFaceUpCastleCards().copy(),
                players.get(povPlayer).getFaceDownCastleCards().size(),
                players.get(otherPlayer).getHand().size(),
                players.get(otherPlayer).getFaceUpCastleCards().copy(),
                players.get(otherPlayer).getFaceDownCastleCards().size(),
                discardPile.getTopCard(),
                deck.isEmpty());
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

        for (Player player : players) {
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

    private List<Player> copyPlayers() {
        List<Player> playersCopy = new ArrayList<>();
        for (Player player : players) {
            playersCopy.add(player.copy());
        }
        return playersCopy;
    }

    public GameState(Deck deck,
                     DiscardPile discardPile,
                     List<Card> burnedCards,
                     List<Player> players,
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
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.handSize = handSize;
        this.castleSize = castleSize;
        this.noOfPlayers = noOfPlayers;
        this.gameOver = gameOver;
        this.lastMove = lastMove;
        this.winningPlayer = winningPlayer;
    }
}
