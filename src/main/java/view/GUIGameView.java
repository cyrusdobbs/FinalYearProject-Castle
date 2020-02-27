package view;

import controller.GUIGameController;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.GameState;
import model.cards.card.Card;

public class GUIGameView {

    @FXML
    private ListView<Card> opponentHand;
    @FXML
    private ListView<Card> opponentFUCards;
    @FXML
    private ListView<Card> opponentFDCards;
    @FXML
    private ListView<Card> playerHand;
    @FXML
    private ListView<Card> playerFUCards;
    @FXML
    private ListView<Card> playerFDCards;

    public void setStage(Stage primaryStage, GameState gameState) throws java.io.IOException {
//        SvgImageLoaderFactory.install();
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/game.fxml"));
//        loader.setController(this);
//
//        Pane root = loader.load();
//
//        primaryStage.setScene(new Scene(root));
//
//        opponentHand.setItems(gameState.getPlayers().get(0).getHand().getCardCollection());
//        opponentHand.setMouseTransparent(true);
//        opponentHand.setFocusTraversable(false);
//        opponentHand.setCellFactory(listView -> new CardListCell(false));
//
//        opponentFUCards.setItems(gameState.getPlayers().get(0).getFaceUpCastleCards().getCardCollection());
//        opponentFUCards.setMouseTransparent(true);
//        opponentFUCards.setFocusTraversable(false);
//        opponentFUCards.setCellFactory(listView -> new CardListCell(false));
//
//        opponentFDCards.setItems(gameState.getPlayers().get(0).getFaceDownCastleCards().getCardCollection());
//        opponentFDCards.setMouseTransparent(true);
//        opponentFDCards.setFocusTraversable(false);
//        opponentFDCards.setCellFactory(listView -> new CardListCell(false));
//
//        playerHand.setItems(gameState.getPlayers().get(1).getHand().getCardCollection());
//        playerHand.setMouseTransparent(true);
//        playerHand.setFocusTraversable(false);
//        playerHand.setCellFactory(listView -> new CardListCell(false));
//
//        playerFUCards.setItems(gameState.getPlayers().get(1).getFaceUpCastleCards().getCardCollection());
//        playerFUCards.setMouseTransparent(true);
//        playerFUCards.setFocusTraversable(false);
//        playerFUCards.setCellFactory(listView -> new CardListCell(false));
//
//        playerFDCards.setItems(gameState.getPlayers().get(1).getFaceDownCastleCards().getCardCollection());
//        playerFDCards.setMouseTransparent(true);
//        playerFDCards.setFocusTraversable(false);
//        playerFDCards.setCellFactory(listView -> new CardListCell(false));
//
//        primaryStage.show();
    }
}
