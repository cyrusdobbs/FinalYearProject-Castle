package view;

import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import model.cards.card.Card;

public class CardListCell extends ListCell<Card> {

    public CardListCell(boolean hidden, EventHandler<MouseEvent> eventHandler) {
        this.hidden = hidden;
        this.eventHandler = eventHandler;
    }

    private Pane pane = new Pane();
    private boolean hidden;
    private EventHandler<MouseEvent> eventHandler;

    @Override
    public void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);
        ImageView imageView = new ImageView();
        Rectangle rectangle = new Rectangle();
        pane.getChildren().add(rectangle);
        imageView.setFitHeight(115);
        imageView.setFitWidth(76);
        if (empty) {
            setGraphic(null);
        } else {
            Image image;
            if (hidden) {
                image = new Image(getClass().getResource("/CardImagesNew/black_joker.svg").toExternalForm(), 76, 115, false, false);
            } else {
                image = new Image(getClass().getResource("/CardImagesNew/" + card.toShortString() + ".svg").toExternalForm(), 76, 115, false, false);
            }
            imageView.setImage(image);
            setGraphic(imageView);
        }
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Tile pressed ");
            event.consume();
        });
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    public Pane getPane() {
        return pane;
    }
}
