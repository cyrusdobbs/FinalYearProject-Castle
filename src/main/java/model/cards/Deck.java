package model.cards;

import model.cards.card.Card;
import model.cards.card.Rank;
import model.cards.card.Suit;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {

    private boolean isShuffled;
    private Stack<Card> deck;

    public Deck() {
        deck = new Stack<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(rank, suit);
                deck.push(card);
            }
        }

        isShuffled = false;
    }

    public Deck(Deck deck) {
        this.isShuffled = deck.isShuffled;
        this.deck = new Stack<>();
        this.deck.addAll(deck.deck);
    }

    public void shuffle() {
        Collections.shuffle(deck);
        isShuffled = true;
    }

    public Card topDeck() {
        if (deck.empty()) {
            return null;
        }

        return deck.pop();
    }

    public boolean isEmpty() {
        return deck.empty();
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    public void replaceWithUnseenCards(List<Card> unseenCards) {
        Stack<Card> newDeck = new Stack<>();
        for (int i = 0; i < deck.size(); i++) {
            newDeck.add(unseenCards.remove(0));
        }
        deck = newDeck;
    }

    @Override
    public String toString() {
        if (deck.empty()) {
            return "EMPTY";
        }

        StringBuilder string = new StringBuilder();
        for (Card card : deck) {
            string.append(card.toShortString()).append(", ");
        }
        if (string.length() > 1) {
            string.setLength(string.length() - 2);
            string.append(" ");
        }
        return string.toString();
    }

    public void updateCount(int[] counts) {
        for (Object ob : deck.toArray()) {
            Card card = (Card) ob;
            counts[card.getRank().getValueCode() - 2]++;
        }
    }
}



