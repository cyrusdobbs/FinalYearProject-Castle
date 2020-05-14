package controller.players.ismcts;

import controller.players.PlayerController;
import moves.*;
import model.cards.Deck;
import model.cards.card.Card;
import model.GameState;
import model.PlayerModel;

import java.util.*;

public abstract class IsmctsPlayerController extends PlayerController {

    protected int maxIterations;
    private double exploration;
    protected boolean verbose;
    protected boolean print;

    public IsmctsPlayerController(PlayerModel playerModel, int playerNo, int maxIterations, boolean verbose, boolean print) {
        super(playerModel, playerNo);
        this.maxIterations = maxIterations;
        exploration = 0.7;
        this.verbose = verbose;
        this.print = print;
    }

    @Override
    public CastleMove getMove(GameState gameState) {
        // This decision is entirely random in all scenarios, therefor hardcoded.
        if (gameState.getPlayerModels().get(gameState.getCurrentPlayer()).getHand().isEmpty()
                && gameState.getPlayerModels().get(gameState.getCurrentPlayer()).getFaceUpCastleCards().isEmpty()) {
            return new PlayFaceDownCastleCard(gameState.getCurrentPlayer(), 0);
        }

        Node rootNode = new Node();
        for (int i = 0; i < maxIterations; i++) {
            Node node = rootNode;

            // Determinise
            GameState currentGameState = cloneAndRandomize(gameState);

            // Selection
            List<CastleMove> availableMovesForSelection = getMoves(currentGameState);
            while (!availableMovesForSelection.isEmpty()
                    && node.getUntriedMoves(availableMovesForSelection).isEmpty()) {

                node = node.UCBSelectChild(availableMovesForSelection, exploration);
                doMove(node.getMove(), currentGameState);
            }

            // Expansion
            List<CastleMove> availableMovesForExpansion = getMoves(currentGameState);
            if (!node.getUntriedMoves(availableMovesForExpansion).isEmpty()) {
                CastleMove move = getRandomMove(node.getUntriedMoves(availableMovesForExpansion));
                int player = currentGameState.getCurrentPlayer();
                doMove(move, currentGameState);
                node.addChild(move, player);
            }

            // Simulation
            simulate(currentGameState);

            // Backpropagation
            while (node != null) {
                node.update(getWinningPlayer(currentGameState));
                node = node.getParentNode();
            }
        }

        printTree(rootNode);

        CastleMove bestMoveFound = rootNode.getChildNodes().stream().max(Comparator.comparing(Node::getWins)).get().getMove();
        return bestMoveFound != null ? bestMoveFound : rootNode.getChildNodes().get(random.nextInt(rootNode.getChildNodes().size())).getMove();
    }

    private void printTree(Node rootNode) {
        if (print) {
            if (verbose) {
                System.out.println(rootNode.treeToString(0));
            } else {
                System.out.println(rootNode.childrenToString());
            }
        }
    }

    protected abstract void simulate(GameState currentGameState);

    protected abstract int getWinningPlayer(GameState currentGameState);

    // IF THIS CHANGES CHANGE IN TERMINAL CONTROLLER
    protected void doMove(CastleMove move, GameState gameState) {
        move.doMove(gameState);
        endTurn(move, gameState);
        gameState.setLastMove(move);
    }

    private void endTurn(CastleMove move, GameState gameState) {
        // If a player burns the pile then they have another go
        if (!move.burnsPile() && !gameState.isGameOver()) {
            gameState.setCurrentPlayer(gameState.getNextPlayer());
        }
    }

    private GameState cloneAndRandomize(GameState gameState) {
        GameState copiedState = gameState.copy();

        int observingPlayer = gameState.getCurrentPlayer();

        List<Card> observedCards = getObservedCards(copiedState);
        List<Card> unseenCards = getUnseenCards(observedCards);

        Collections.shuffle(unseenCards);
        copiedState.getDeck().replaceWithUnseenCards(unseenCards);
        for (int playerNo = 0; playerNo < copiedState.getPlayerModels().size(); playerNo++) {
            PlayerModel player = copiedState.getPlayerModels().get(playerNo);
            if (playerNo != observingPlayer) {
                player.getHand().replaceWithUnseenCards(unseenCards);
            }
            player.getFaceDownCastleCards().replaceWithUnseenCards(unseenCards);
        }

        copiedState.isStateValid();
        return copiedState;
    }

    private List<Card> getUnseenCards(List<Card> observedCards) {
        Deck deckToCompare = new Deck();
        List<Card> unseenCards = new ArrayList<>();
        for (Card card : new ArrayList<>(deckToCompare.getDeck())) {
            if (!containsCard(observedCards, card)) {
                unseenCards.add(card);
            }
        }
        return unseenCards;
    }

    private boolean containsCard(List<Card> list, Card card) {
        return list.stream().anyMatch(o -> o.getRank().getShortName().equals(card.getRank().getShortName())
                && o.getSuit().getShortName().equals(card.getSuit().getShortName()));
    }

    private List<Card> getObservedCards(GameState gameState) {
        List<PlayerModel> players = gameState.getPlayerModels();
        // Add observing players hand
        List<Card> observedCards = new ArrayList<>(players.get(gameState.getCurrentPlayer()).getHand().getCardCollection());
        // Add each players face up castle cards
        for (PlayerModel player : players) {
            observedCards.addAll(player.getFaceUpCastleCards().getCardCollection());
        }
        // Player can see what cards have been discarded (all for now)
        // TODO: Change number of discarded model.cards that can be remembered
        observedCards.addAll(gameState.getDiscardPile().getPlayedPile());
        observedCards.addAll(gameState.getBurnedCards());
        return observedCards;
    }
}
