package controller;

import controller.players.*;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.GameState;
import model.Player;
import model.cards.card.Card;
import moves.CastleMove;
import moves.InvalidMove;
import view.CardListCell;
import view.GUIGameView;


import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GUIGameController extends Application implements EventHandler<MouseEvent> {

    private static GameState gameState;
    private static GUIGameView gameView;

    private static List<PlayerController> players;

    private static HumanPlayerController humanPlayer;
    private static AIController AIPlayer;

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
    @FXML
    private Button playCardsButton;
    @FXML
    private Text textBox;

    private List<Card> selectedCards = new ArrayList<>();
    private String cardsFrom = "";

    private List<CardListCell> selectedCells = new ArrayList<>();

    private static PropertyChangeSupport propertyChangeSupport;
    private BooleanProperty AITurn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/game.fxml"));
        loader.setController(this);

        Pane root = loader.load();

        primaryStage.setScene(new Scene(root));

        propertyChangeSupport = new PropertyChangeSupport(this);
        AITurn = new SimpleBooleanProperty(false);
        AITurn.addListener((observable, oldValue, newValue) -> {
            if (!oldValue && newValue) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                doMove(AIPlayer.getMove(gameState));
            }
            AITurn.setValue(false);
        });


        opponentHand.setItems(gameState.getPlayers().get(1).getHand().getCardCollection());
        opponentFUCards.setMouseTransparent(true);
        opponentHand.setFocusTraversable(false);
        opponentHand.setCellFactory(listView -> new CardListCell(false, this));

        opponentFUCards.setItems(gameState.getPlayers().get(1).getFaceUpCastleCards().getCardCollection());
        opponentFUCards.setMouseTransparent(true);
        opponentFUCards.setFocusTraversable(false);
        opponentFUCards.setCellFactory(listView -> new CardListCell(false, this));

        opponentFDCards.setItems(gameState.getPlayers().get(1).getFaceDownCastleCards().getCardCollection());
        opponentFDCards.setMouseTransparent(true);
        opponentFDCards.setFocusTraversable(false);
        opponentFDCards.setCellFactory(listView -> new CardListCell(false, this));

        playerHand.setItems(gameState.getPlayers().get(0).getHand().getCardCollection());
        playerHand.setFocusTraversable(false);
        playerHand.setCellFactory(listView -> new CardListCell(false, this));
        playerHand.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playerHand.setOnMouseClicked((EventHandler<Event>) event -> {
            selectedCards =  playerHand.getSelectionModel().getSelectedItems();
            cardsFrom = ControllerConstants.HAND;
        });

        playerFUCards.setItems(gameState.getPlayers().get(0).getFaceUpCastleCards().getCardCollection());
        playerFUCards.setFocusTraversable(false);
        playerFUCards.setCellFactory(listView -> new CardListCell(false, this));
        playerFUCards.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playerFUCards.setOnMouseClicked((EventHandler<Event>) event -> {
            selectedCards =  playerFUCards.getSelectionModel().getSelectedItems();
            cardsFrom = ControllerConstants.FU_CASTLE;
        });

        playerFDCards.setItems(gameState.getPlayers().get(0).getFaceDownCastleCards().getCardCollection());
        playerFDCards.setFocusTraversable(false);
        playerFDCards.setCellFactory(listView -> new CardListCell(false, this));
        playerFDCards.setOnMouseClicked((EventHandler<Event>) event -> {
            selectedCards =  playerFDCards.getSelectionModel().getSelectedItems();
            cardsFrom = ControllerConstants.FD_CASTLE;
        });

        playCardsButton.setOnAction(event -> {
            if (gameState.getCurrentPlayer() == 1) {
                textBox.setText("Wait for your turn.");
                return;
            }

            if (!selectedCards.isEmpty()) {
                CastleMove move = humanPlayer.getMove(gameState, selectedCards, cardsFrom);

                if (move instanceof InvalidMove) {
                    textBox.setText(((InvalidMove) move).getMessage());
                } else {
                    doMove(move);
                    AITurn.setValue(true);
                }
            }
        });


        primaryStage.show();
    }

    // Needed
    public void doMove(CastleMove move) {
        move.doMove(gameState);
        // If a player burns the pile then they have another go
        if (!move.burnsPile() && !gameState.isGameOver()) {
            gameState.setCurrentPlayer(gameState.getNextPlayer());
        }
        gameState.setLastMove(move);
    }

    public static void main(String[] args) {
        SvgImageLoaderFactory.install();
        List<Player> playerModels = new ArrayList<>();
        playerModels.add(new Player("Player"));
        playerModels.add(new Player("AI"));

        gameState = new GameState(playerModels);
        gameView = new GUIGameView();
        players = new ArrayList<>();
//        AIPlayer = new IsmctsPlayerController(playerModels.get(1), 2500, false, true);
        AIPlayer = new LowestPlayerController(playerModels.get(1));
        humanPlayer = new HumanPlayerController(playerModels.get(0), null);

        launch(args);
    }

    @Override
    public void handle(MouseEvent event) {
        CardListCell cell = (CardListCell) event.getSource();
        selectedCells.add(cell);
    }
}
