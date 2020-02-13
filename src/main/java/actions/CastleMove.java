package actions;

import model.GameState;
import model.cards.card.Card;

import java.util.ArrayList;
import java.util.List;

public abstract class CastleMove {

    public int player;
    List<Card> cards;

    boolean burnsPile;

    public boolean burnsPile() {
        return burnsPile;
    }

    public CastleMove(int player, List<Card> cards) {
        this.player = player;
        this.cards = cards;
    }

    public CastleMove(int player) {
        this.player = player;
        this.cards = new ArrayList<>();
    }

    public String getCardsToString() {
        if (cards.isEmpty()) {
            return "Pick up";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards) {
            stringBuilder.append(card.toShortString()).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public abstract void doAction(GameState gameState);

    public abstract String toString();
}
