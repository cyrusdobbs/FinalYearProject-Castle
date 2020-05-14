package controller.players;

import model.GameState;
import model.PlayerModel;
import model.cards.card.Card;
import moves.*;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;

public abstract class PlayerController {

    protected int playerNo;
    protected PlayerModel playerModel;

    protected Random random;

    public PlayerController(PlayerModel playerModel, int playerNo) {
        this.playerModel = playerModel;
        this.playerNo = playerNo;
        random = new Random();
    }

    public abstract CastleMove getMove(GameState gameState);

    public abstract PlayerController copy();

    protected List<CastleMove> getMoves(GameState gameState) {
        PlayerModel playerHasGo = gameState.getPlayerModels().get(gameState.getCurrentPlayer());
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

    protected List<CastleMove> getPickCastleMoves(PlayerModel player, GameState gameState) {
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

    protected List<CastleMove> getPickCardToPlayMoves(PlayerModel player, GameState gameState) {
        List<CastleMove> playHandCardMoves = new ArrayList<>();

        Map<String, List<Card>> legalCards = player.getHand().getLegalCardsGrouped(gameState.getDiscardPile().getTopCard());
        for (List<Card> groupOfLegalCards : legalCards.values()) {
            playHandCardMoves.add(new PlayHandCard(gameState.getCurrentPlayer(), groupOfLegalCards));
        }
        return playHandCardMoves.isEmpty() ? Collections.singletonList(new PickUp(gameState.getCurrentPlayer())) : playHandCardMoves;
    }

    protected List<CastleMove> getPickFUCastleCardToPlayMoves(PlayerModel player, GameState gameState) {
        List<CastleMove> playFUCastleCardMoves = new ArrayList<>();

        Map<String, List<Card>> legalCards = player.getFaceUpCastleCards().getLegalCardsGrouped(gameState.getDiscardPile().getTopCard());
        for (List<Card> groupOfLegalCards : legalCards.values()) {
            playFUCastleCardMoves.add(new PlayFaceUpCastleCard(gameState.getCurrentPlayer(), groupOfLegalCards));
        }
        return playFUCastleCardMoves.isEmpty() ? Collections.singletonList(new PickUp(gameState.getCurrentPlayer())) : playFUCastleCardMoves;
    }

    protected List<CastleMove> getPickFDCastleCardToPlayMoves(PlayerModel player, GameState gameState) {
        List<CastleMove> playFDCastleCardMoves = new ArrayList<>();

        for (int i = 0; i < player.getFaceDownCastleCards().size(); i++) {
            playFDCastleCardMoves.add(new PlayFaceDownCastleCard(gameState.getCurrentPlayer(), i));
        }
        return playFDCastleCardMoves;
    }

    protected CastleMove getRandomMove(List<CastleMove> moves) {
        return moves.get(random.nextInt(moves.size()));
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public void setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
    }
}
