package model;

import model.cards.CardCollection;
import model.cards.FaceUpCastleCards;
import model.cards.Hand;
import model.cards.card.Card;
import sqlexporter.Entry;
import util.CastleConstants;

import java.util.Arrays;
import java.util.Comparator;

public class SimpleGameState {

    private Hand hand;
    private FaceUpCastleCards castleFU;
    private int castleFDSize;
    private int opHandSize;
    private FaceUpCastleCards opCastleFU;
    private int opCastleFDSize;
    private Card topCard;
    private boolean deckEmpty;

    public SimpleGameState(Hand hand, FaceUpCastleCards castleFU, int castleFDSize, int opHandSize, FaceUpCastleCards opCastleFU, int opCastleFDSize, Card topCard, boolean deckEmpty) {
        this.hand = hand;
        this.castleFU = castleFU;
        this.castleFDSize = castleFDSize;
        this.opHandSize = opHandSize;
        this.opCastleFU = opCastleFU;
        this.opCastleFDSize = opCastleFDSize;
        this.topCard = topCard;
        this.deckEmpty = deckEmpty;
    }

    public Entry toEntry(boolean won) {
        Entry newEntry = new Entry();
        newEntry.setHand(cardsToStringArray(hand));
        newEntry.setCastleFu(cardsToStringArray(castleFU));
        newEntry.setCastleFdSize(castleFDSize);
        newEntry.setOpHandSize(opHandSize);
        newEntry.setOpCastleFu(cardsToStringArray(opCastleFU));
        newEntry.setOpCastleFdSize(opCastleFDSize);
        newEntry.setTop(topCard == null ? CastleConstants.NO_CARDS : String.valueOf(topCard.getRank().getValueCode()));
        newEntry.setDeckEmpty(deckEmpty);
        newEntry.setWon(won);
        return newEntry;
    }

    private static String[] cardsToStringArray(CardCollection cards) {
        if (cards.isEmpty()) {
            return new String[]{};
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards.getCardCollection()) {
            stringBuilder.append(card.getRank().getValueCode()).append(CastleConstants.LIST_DELIMITER);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        String[] array = stringBuilder.toString().split("\\|");
        Arrays.sort(array, Comparator.comparingInt(Integer::parseInt));
        return array;
    }

    public Hand getHand() {
        return hand;
    }

    public FaceUpCastleCards getCastleFU() {
        return castleFU;
    }

    public int getCastleFDSize() {
        return castleFDSize;
    }

    public int getOpHandSize() {
        return opHandSize;
    }

    public FaceUpCastleCards getOpCastleFU() {
        return opCastleFU;
    }

    public int getOpCastleFDSize() {
        return opCastleFDSize;
    }

    public Card getTopCard() {
        return topCard;
    }

    public boolean isDeckEmpty() {
        return deckEmpty;
    }
}
