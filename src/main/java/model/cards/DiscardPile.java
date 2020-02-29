package model.cards;

import model.cards.card.Card;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {

    private List<Card> playedPile;

    public DiscardPile() {
        playedPile = new ArrayList<>();
    }

    public DiscardPile(DiscardPile discardPile) {
        this.playedPile = new ArrayList<>();
        this.playedPile.addAll(discardPile.playedPile);
    }

    public Card getTopCard() {
        if (playedPile.isEmpty()) {
            return null;
        }
        return playedPile.get(0);
    }

    public List<Card> pickUp() {
        List<Card> pileToReturn = new ArrayList<>(playedPile);
        playedPile = new ArrayList<>();
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
            playedPile.add(0, card);
        }
    }

    public List<Card> getPlayedPile() {
        return playedPile;
    }

    public void burn() {
        playedPile.clear();
    }
}
