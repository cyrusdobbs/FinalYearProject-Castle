package model.cards;


import model.cards.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DiscardPile {

    private Stack<Card> playedPile;

    public DiscardPile() {
        playedPile = new Stack<>();
    }

    public DiscardPile(DiscardPile discardPile) {
        this.playedPile = new Stack<>();
        this.playedPile.addAll(discardPile.playedPile);
    }

    public Card getTopCard() {
        if (playedPile.empty()) {
            return null;
        }
        return playedPile.peek();
    }

    public List<Card> pickUp() {
        List<Card> pileToReturn = new ArrayList<>(playedPile);
        playedPile = new Stack<>();
        return pileToReturn;
    }

    public boolean isSameTopFourCards() {
        if (playedPile.size() < 4) {
            return false;
        }
        int value = getTopCard().getRank().getValueCode();
        return (value == playedPile.get(playedPile.size() - 2).getRank().getValueCode())
                && (value == playedPile.get(playedPile.size() - 3).getRank().getValueCode())
                && (value == playedPile.get(playedPile.size() - 4).getRank().getValueCode());
    }

    public void play(List<Card> playedCards) {
        for (Card card : playedCards) {
            playedPile.push(card);
        }
    }

    public Stack<Card> getPlayedPile() {
        return playedPile;
    }

    public void burn() {
        playedPile.clear();
    }
}
