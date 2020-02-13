package controller.players;

import moves.*;
import model.GameState;
import model.Player;
import model.cards.card.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LowestPlayerController extends PlayerController {

    public LowestPlayerController(Player playerModel) {
        super(playerModel);
    }

    @Override
    public CastleMove getMove(GameState gameState) {
        if (!playerModel.hasPickedCastle()) {
            return pickCastle(gameState);
        } else if (!playerModel.getHand().isEmpty()){
            return pickCardToPlay(gameState);
        } else if (!playerModel.getFaceUpCastleCards().isEmpty()) {
            return pickFUCastleCardToPlay(gameState);
        } else {
            return pickFDCastleCardToPlay(gameState);
        }
    }

    private CastleMove pickCardToPlay(GameState gameState) {
        playerModel.getHand().sortCardsByStrength();
        List<Card> cardsPlayed = new ArrayList<>();
        for (Card card : playerModel.getHand().getCardCollection()) {
            if (cardsPlayed.isEmpty() && card.isCardValid(gameState.getDiscardPile().getTopCard())) {
                cardsPlayed.add(card);
            } else if (!cardsPlayed.isEmpty() && cardsPlayed.get(0).getRank().getValueCode() == card.getRank().getValueCode()) {
                cardsPlayed.add(card);
            } else if (!cardsPlayed.isEmpty()){
                break;
            }
        }
        return !cardsPlayed.isEmpty() ? new PlayHandCard(gameState.getCurrentPlayer(), cardsPlayed) : new PickUp(gameState.getCurrentPlayer());
    }

    private CastleMove pickCastle(GameState gameState) {
        List<Card> castle = new ArrayList<>();
        playerModel.getHand().sortCardsByStrength();
        for (int i = playerModel.getHand().size() - 1; i > gameState.getHandSize() - 1; i--) {
            castle.add(playerModel.getHand().getCardCollection().get(i));
        }
        castle.sort(Comparator.comparing(o -> o.getRank().getStrength()));
        return new PickCastle(gameState.getCurrentPlayer(), castle);
    }

    private CastleMove pickFUCastleCardToPlay(GameState gameState) {
        List<Card> cardsPlayed = new ArrayList<>();
        for (Card card : playerModel.getFaceUpCastleCards().getCardCollection()) {
            if (cardsPlayed.isEmpty() && card.isCardValid(gameState.getDiscardPile().getTopCard())) {
                cardsPlayed.add(card);
            } else if (!cardsPlayed.isEmpty() && cardsPlayed.get(0).getRank().getValueCode() == card.getRank().getValueCode()) {
                cardsPlayed.add(card);
            } else if (!cardsPlayed.isEmpty()){
                break;
            }
        }
        return !cardsPlayed.isEmpty() ? new PlayFaceUpCastleCard(gameState.getCurrentPlayer(), cardsPlayed) : new PickUp(gameState.getCurrentPlayer());
    }

    private CastleMove pickFDCastleCardToPlay(GameState gameState) {
        return new PlayFaceDownCastleCard(gameState.getCurrentPlayer(), 0);
    }
}
