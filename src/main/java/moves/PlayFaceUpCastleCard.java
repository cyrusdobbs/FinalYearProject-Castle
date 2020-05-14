package moves;

import model.GameState;
import model.cards.card.Card;

import java.util.List;

public class PlayFaceUpCastleCard extends PlayCard {

    public PlayFaceUpCastleCard(int player, List<Card> cardsPlayed) {
        super(player, cardsPlayed);
    }

    @Override
    public void doMove(GameState gameState) {
        // Remove card from hand
        gameState.getPlayerModels().get(player).getFaceUpCastleCards().removeCards(cards);
        // Play card
        super.doMove(gameState);
    }

    @Override
    public String toString() {
        return super.toString() + "FUCastle" + (burnsPile ? " and BURNED the pile." : ".");
    }

    @Override
    public String toHumanString() {
        return super.toHumanString() + "FUCastle.";
    }
}
