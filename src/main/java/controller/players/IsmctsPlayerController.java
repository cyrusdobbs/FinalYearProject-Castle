package controller.players;

import controller.TerminalGameController;
import moves.*;
import model.cards.Deck;
import model.cards.card.Card;
import model.GameState;
import model.Player;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;

public class IsmctsPlayerController extends PlayerController implements AIController {

    private int maxIterations;
    private double exploration;
    private boolean verbose;
    private Random random;
    private boolean print;

    public IsmctsPlayerController(Player playerModel, int maxIterations, boolean verbose, boolean print) {
        super(playerModel);
        this.maxIterations = maxIterations;
        exploration = 0.7;
        this.verbose = verbose;
        random = new Random();
        this.print = print;
    }

    @Override
    public CastleMove getMove(GameState gameState) {
        // This decision is entirely random in all scenarios, therefor hardcoded.
        if (gameState.getPlayers().get(gameState.getCurrentPlayer()).getHand().isEmpty()
                && gameState.getPlayers().get(gameState.getCurrentPlayer()).getFaceUpCastleCards().isEmpty()) {
            return new PlayFaceDownCastleCard(gameState.getCurrentPlayer(), 0);
        }

        Node rootNode = new Node();
        for (int i = 0; i < maxIterations; i++) {
            Node node = rootNode;

            // Determinise
            GameState currentGameState = cloneAndRandomize(gameState);

            // Select
            while (!getMoves(currentGameState).isEmpty() && node.getUntriedMoves(getMoves(currentGameState)).isEmpty()) {
                node = node.UCBSelectChild(getMoves(currentGameState), exploration);
                doMove(node.getMove(), currentGameState);
            }

            // Expand
            if (!node.getUntriedMoves(getMoves(currentGameState)).isEmpty()) {
                CastleMove move = getRandomMove(node.getUntriedMoves(getMoves(currentGameState)));
                int player = currentGameState.getCurrentPlayer();
                doMove(move, currentGameState);
                node.addChild(move, player);
            }

            // Simulate
            while (!getMoves(currentGameState).isEmpty()) {
                doMove(getRandomMove(getMoves(currentGameState)), currentGameState);
            }

            // Backpropagate
            while (node != null) {
                node.update(currentGameState);
                node = node.getParentNode();
            }
        }

        if (print) {
            if (verbose) {
                System.out.println(rootNode.treeToString(0));
            } else {
                System.out.println(rootNode.childrenToString());
            }
        }

        CastleMove bestMoveFound = rootNode.getChildNodes().stream().max(Comparator.comparing(Node::getWins)).get().getMove();
        return bestMoveFound != null ? bestMoveFound : rootNode.getChildNodes().get(random.nextInt(rootNode.getChildNodes().size())).getMove();
    }

    // IF THIS CHANGES CHANGE IN TERMINAL CONTROLLER
    private void doMove(CastleMove move, GameState gameState) {
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
        for (int i = 0; i < copiedState.getPlayers().size(); i++) {
            Player player = copiedState.getPlayers().get(i);
            if (i != observingPlayer) {
                player.getHand().replaceWithUnseenCards(unseenCards);
            }
            player.getFaceDownCastleCards().replaceWithUnseenCards(unseenCards);

        }
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
        List<Player> players = gameState.getPlayers();
        // Add observing players hand
        List<Card> observedCards = new ArrayList<>(players.get(gameState.getCurrentPlayer()).getHand().getCardCollection());
        // Add each players face up castle cards
        for (Player player : players) {
            observedCards.addAll(player.getFaceUpCastleCards().getCardCollection());
        }
        // Player can see what cards have been discarded (all for now)
        // TODO: Change number of discarded model.cards that can be remembered
        observedCards.addAll(gameState.getDiscardPile().getPlayedPile());
        return observedCards;
    }

    private List<CastleMove> getMoves(GameState gameState) {
        Player playerHasGo = gameState.getPlayers().get(gameState.getCurrentPlayer());
        if (!playerHasGo.hasPickedCastle()) {
            return getPickCastleMoves(playerHasGo, gameState);
        } else if (!playerHasGo.getHand().isEmpty()) {
            return getPickCardToPlayMoves(playerHasGo, gameState);
        } else if (!playerHasGo.getFaceUpCastleCards().isEmpty()) {
            return getPickFUCastleCardToPlayMoves(playerHasGo, gameState);
        } else if (!playerHasGo.getFaceDownCastleCards().isEmpty()) {
            return getPickFDCastleCardToPlayMoves(playerHasGo, gameState);
        }
        return new ArrayList<>();
    }

    private List<CastleMove> getPickCastleMoves(Player player, GameState gameState) {
        List<CastleMove> pickCastleMoves = new ArrayList<>();

        Iterator<int[]> cardCombinations = CombinatoricsUtils.combinationsIterator(player.getHand().size(), gameState.getCastleSize());
        while (cardCombinations.hasNext()) {
            int[] cardCombination = cardCombinations.next();
            List<Card> faceUpCastleCards = new ArrayList<>();
            for (int cardIndex : cardCombination) {
                faceUpCastleCards.add(player.getHand().getCardCollection().get(cardIndex));
            }
            pickCastleMoves.add(new PickCastle(gameState.getCurrentPlayer(), faceUpCastleCards));
        }
        return pickCastleMoves;
    }

    private List<CastleMove> getPickCardToPlayMoves(Player player, GameState gameState) {
        List<CastleMove> playHandCardMoves = new ArrayList<>();

        Map<String, List<Card>> legalCards = player.getHand().getLegalCardsGrouped(gameState.getDiscardPile().getTopCard());
        for (List<Card> groupOfLegalCards : legalCards.values()) {
            playHandCardMoves.add(new PlayHandCard(gameState.getCurrentPlayer(), groupOfLegalCards));
        }
        return playHandCardMoves.isEmpty() ? Collections.singletonList(new PickUp(gameState.getCurrentPlayer())) : playHandCardMoves;
    }

    private List<CastleMove> getPickFUCastleCardToPlayMoves(Player player, GameState gameState) {
        List<CastleMove> playFUCastleCardMoves = new ArrayList<>();

        Map<String, List<Card>> legalCards = player.getFaceUpCastleCards().getLegalCardsGrouped(gameState.getDiscardPile().getTopCard());
        for (List<Card> groupOfLegalCards : legalCards.values()) {
            playFUCastleCardMoves.add(new PlayFaceUpCastleCard(gameState.getCurrentPlayer(), groupOfLegalCards));
        }
        return playFUCastleCardMoves.isEmpty() ? Collections.singletonList(new PickUp(gameState.getCurrentPlayer())) : playFUCastleCardMoves;
    }

    private List<CastleMove> getPickFDCastleCardToPlayMoves(Player player, GameState gameState) {
        List<CastleMove> playFDCastleCardMoves = new ArrayList<>();

        for (int i = 0; i < player.getFaceDownCastleCards().size(); i++) {
            playFDCastleCardMoves.add(new PlayFaceDownCastleCard(gameState.getCurrentPlayer(), i));
        }
        return playFDCastleCardMoves;
    }

    private CastleMove getRandomMove(List<CastleMove> moves) {
        return moves.get(random.nextInt(moves.size()));
    }
}
