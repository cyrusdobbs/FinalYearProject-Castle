package model.cards;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.cards.card.Card;


public class Hand extends CardCollection {

    public Hand() {
    }

    public Hand(ObservableList<Card> cards) {
        super(cards);
    }

    @Override
    public void printOptions(boolean offerPickup, boolean hidden) {
        System.out.println("Hand: ");
        super.printOptions(offerPickup, hidden);
    }

//    public Hand copy() {
//        List<Card> newCollection = new ArrayList<>();
//        for (Card model.cards.card : getCardCollection()) {
//            newCollection.add(new Card(model.cards.card));
//        }
//        return new Hand(newCollection);
//    }

    public Hand copy() {
        ObservableList<Card> newCollection = FXCollections.observableArrayList(getCardCollection());
        return new Hand(newCollection);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
