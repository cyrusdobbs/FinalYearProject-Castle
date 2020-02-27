package controller.players;

import controller.ControllerConstants;
import model.GameState;
import model.Player;
import model.cards.card.Card;
import moves.*;
import view.players.HumanPlayerView;

import java.util.List;

public class HumanPlayerController extends PlayerController {

    private HumanPlayerView playerView;

    private static final String PICK_FU_CARD_NOT_FROM_FU_CARDS = "Pick card(s) from your face-up castle.";
    private static final String PICK_FD_CARD_NOT_FROM_FD_CARDS = "Pick card(s) from your face-down castle.";

    private static final String PICK_CARD_NOT_FROM_HAND = "Pick card(s) from your hand.";
    private static final String PICK_CARD_NOT_ALL_CARDS_SAME = "All cards must be the same face value.";
    private static final String PICK_CARD_INVALID = "Cannot play that card.";

    public HumanPlayerController(Player playerModel, HumanPlayerView playerView) {
        super(playerModel);
        this.playerView = playerView;
    }


    public CastleMove getMove(GameState gameState, List<Card> selectedCards, String cardsFrom) {
        if (!playerModel.hasPickedCastle()) {
            return getPickCastle(gameState, selectedCards, cardsFrom);
        } else if (!playerModel.getHand().isEmpty()) {
            return getCardToPlay(gameState, selectedCards, cardsFrom);
        } else if (!playerModel.getFaceUpCastleCards().isEmpty()) {
            return getFUCardToPlay(gameState, selectedCards, cardsFrom);
        } else {
            return getFDCastleCardToPlay(gameState, selectedCards, cardsFrom);
        }
    }

    private CastleMove getPickCastle(GameState gameState, List<Card> selectedCards, String cardsFrom) {
        if (!cardsFrom.equals(ControllerConstants.HAND)) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_CARD_NOT_FROM_HAND);
        } else if (selectedCards.size() != gameState.getCastleSize()) {
            return new InvalidMove(gameState.getCurrentPlayer(), "Pick " + gameState.getCastleSize() + " cards.");
        } else {
            return new PickCastle(gameState.getCurrentPlayer(), selectedCards);
        }
    }

    private CastleMove getCardToPlay(GameState gameState, List<Card> selectedCards, String cardsFrom) {
        if (!cardsFrom.equals(ControllerConstants.HAND)) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_CARD_NOT_FROM_HAND);
        } else if (!allCardsTheSame(selectedCards)) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_CARD_NOT_ALL_CARDS_SAME);
        } else if (!selectedCards.get(0).isCardValid(gameState.getDiscardPile().getTopCard())) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_CARD_INVALID);
        } else {
            return new PlayHandCard(gameState.getCurrentPlayer(), selectedCards);
        }
    }

    private CastleMove getFUCardToPlay(GameState gameState, List<Card> selectedCards, String cardsFrom) {
        if (!cardsFrom.equals(ControllerConstants.FU_CASTLE)) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_FU_CARD_NOT_FROM_FU_CARDS);
        } else if (!allCardsTheSame(selectedCards)) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_CARD_NOT_ALL_CARDS_SAME);
        } else if (!selectedCards.get(0).isCardValid(gameState.getDiscardPile().getTopCard())) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_CARD_INVALID);
        } else {
            return new PlayFaceUpCastleCard(gameState.getCurrentPlayer(), selectedCards);
        }
    }

    private CastleMove getFDCastleCardToPlay(GameState gameState, List<Card> selectedCards, String cardsFrom) {
        if (!cardsFrom.equals(ControllerConstants.FD_CASTLE)) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_FD_CARD_NOT_FROM_FD_CARDS);
        } else if (!selectedCards.get(0).isCardValid(gameState.getDiscardPile().getTopCard())) {
            return new InvalidMove(gameState.getCurrentPlayer(), PICK_CARD_INVALID);
        } else {
            return new PlayFaceDownCastleCard(gameState.getCurrentPlayer(), playerModel.getFaceDownCastleCards().getCardCollection().indexOf(selectedCards.get(0)));
        }
    }

    private boolean allCardsTheSame(List<Card> selectedCards) {
        Card firstCard = selectedCards.get(0);
        for (Card card : selectedCards) {
            if (!card.getRank().equals(firstCard.getRank())) {
                return false;
            }
        }
        return true;
    }
}
