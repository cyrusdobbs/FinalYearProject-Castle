package moves;

import model.GameState;
import model.cards.card.Card;

import java.util.List;

public class PlayFaceUpCastleCard extends PlayCard {

    public PlayFaceUpCastleCard(int player, Card cardPlayed) {
        super(player, cardPlayed);
    }

    public PlayFaceUpCastleCard(int player, List<Card> cardsPlayed) {
        super(player, cardsPlayed);
    }


    @Override
    public void doMove(GameState gameState) {
        // Remove model.cards.card from hand
        gameState.getPlayers().get(player).getFaceUpCastleCards().removeCards(cards);
        // Play model.cards.card
        super.doMove(gameState);
    }

    @Override
    public String toString() {
        return super.toString() + "FUCastle" + (burnsPile ? " and BURNED the pile." : ".");
    }
}
