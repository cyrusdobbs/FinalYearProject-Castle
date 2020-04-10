package model.cards;


import model.cards.card.Card;

import java.util.*;
import java.util.stream.Collectors;


public abstract class CardCollection {

    private List<Card> cardCollection;

    CardCollection() {
        cardCollection = new ArrayList<>();
    }

    CardCollection(List<Card> cards) {
        cardCollection = cards;
    }

    public List<Card> getCardCollection() {
        return cardCollection;
    }

    public void addCard(Card card) {
        cardCollection.add(card);
    }

    public void addCards(List<Card> cards) {
        cardCollection.addAll(cards);
    }

    public void removeCards(List<Card> cards) {
        cardCollection.removeAll(cards);
    }

    public void removeCard(Card card) {
        cardCollection.remove(card);
    }

    public int size() {
        return cardCollection.size();
    }

    public boolean isEmpty() {
        return cardCollection.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Card card : cardCollection) {
            string.append(card.toShortString()).append(", ");
        }
        if (string.length() > 1) {
            string.setLength(string.length() - 2);
        }
        return string.toString();
    }

    public String toHiddenString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < cardCollection.size(); i++) {
            string.append("X").append(", ");
        }
        if (string.length() > 1) {
            string.setLength(string.length() - 2);
        }
        return string.toString();
    }

    public void printOptions(boolean offerPickup, boolean hidden) {
        if (isEmpty()) {
            System.out.println("1: " + "Pick up");
        } else {
            int cardNo = 1;
            for (Card card : cardCollection) {
                System.out.println(cardNo + ": " + (hidden ? "X" : card.toShortString()));
                cardNo++;
            }
            if (offerPickup) {
                System.out.println(cardNo + ": " + "Pick up");
            }
        }
        System.out.println();
    }

    public void replaceWithUnseenCards(List<Card> unseenCards) {
        List<Card> newCardCollection = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            newCardCollection.add(unseenCards.remove(0));
        }
        cardCollection = newCardCollection;
    }

    public Map<String, List<Card>> getLegalCardsGrouped(Card topCard) {
        List<Card> legalCards = getLegalCards(topCard);
        return legalCards.stream().collect(Collectors.groupingBy(w -> w.getRank().getShortName()));
    }

    public void sortCardsByStrength() {
        cardCollection.sort(Comparator.comparing(o -> o.getRank().getStrength()));
    }

    private List<Card> getLegalCards(Card topCard) {
        List<Card> legalCards = new ArrayList<>();
        for (Card card : cardCollection) {
            if (card.isCardValid(topCard)) {
                legalCards.add(card);
            }
        }
        return legalCards;
    }

    public void updateCount(int[] counts) {
        for (Card card : getCardCollection()) {
            counts[card.getRank().getValueCode() - 2]++;
        }
    }

//    protected Card chooseCard(String from) {
//        System.out.println("Which model.cards.card would you like to swap?");
//        return removeCard(getParsedIntegerInput("1|2|3"));
//    }
}
