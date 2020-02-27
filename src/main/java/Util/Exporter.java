package Util;

import com.opencsv.CSVWriter;
import model.Game;
import model.SimpleGameState;
import model.cards.CardCollection;
import model.cards.card.Card;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Exporter {

    private static final int ENTRIES_PER_FILE = 50000;
    private static final String LIST_DELIMITER = ",";
    private static final char SEPARATOR = '|';
    private CSVWriter csvWriter;

    private String fileName;
    private int fileCount;
    private int entryCount;


    public Exporter(String fileName) throws IOException {
        this.fileName = fileName;
        fileCount = 0;
        entryCount = 0;

        newFile();
        String[] header = {"HAND", "CASTLE", "OP_CASTLE", "TOP", "WON"};
        csvWriter.writeNext(header);
    }

    public void exportGame(Game game) throws IOException {
        if (entryCount > ENTRIES_PER_FILE) {
            newFile();
        }

        for (SimpleGameState gameState : game.getGameStates()) {
            String hand = cardsToString(gameState.getHand());
            String castle = cardsToString(gameState.getCastle());
            String opCastle = cardsToString(gameState.getOpCastle());
            String faceUp = gameState.getFaceUpCard() == null ? "NA" : gameState.getFaceUpCard().getRank().getShortName();
            String won = game.hasWon() ? "W" : "L";
            csvWriter.writeNext(new String[]{hand, castle, opCastle, faceUp, won});
            entryCount++;
        }
    }

    private String cardsToString(CardCollection cards) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Card card : cards.getCardCollection()) {
            stringBuilder.append(card.getRank().getShortName()).append(LIST_DELIMITER);
        }
        return stringBuilder.toString();
    }

    private void newFile() throws IOException {
        csvWriter.close();

        Writer writer = Files.newBufferedWriter(Paths.get(fileName + fileCount + ".csv"));

        csvWriter = new CSVWriter(writer,
                SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        fileCount++;
    }

    public void close() throws IOException {
        csvWriter.close();
    }
}
