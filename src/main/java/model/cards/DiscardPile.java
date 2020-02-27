package model.cards;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.cards.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DiscardPile {

    private ObservableList<Card> playedPile;

    public DiscardPile() {
        playedPile = FXCollections.observableArrayList();
    }

    public DiscardPile(DiscardPile discardPile) {
        this.playedPile = FXCollections.observableArrayList();
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
        playedPile = FXCollections.observableArrayList();
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
