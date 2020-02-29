package util;

import com.opencsv.CSVWriter;
import model.GameHistory;
import model.SimpleGameState;
import model.cards.CardCollection;
import model.cards.card.Card;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Exporter {

    // 50,000 is for 30 minutes of games
    private static final int ENTRIES_PER_FILE = 2500;
    private static final char LIST_DELIMITER = '|';
    private static final String OUTPUT_FOLDER = "output/";
    private static final String TRUE = "1";
    private static final String FALSE = "0";
    private static final String[] HEADER = {"HAND", "CASTLE_FU", "CASTLE_FD_SIZE", "OP_HAND_SIZE", "OP_CASTLE_FU", "OP_CASTLE_FD_SIZE", "TOP", "DECK_EMPTY", "WON"};
    private static final String NA = "NA";
    private static final String NO_CARDS = "";
    private static final String SUMMARY_FILE_NAME = "Summary";
    private static final String CSV_EXT = ".csv";

    private CSVWriter csvWriter;

    private String fileName;
    private int fileCount;
    private int entryCount;

    public Exporter(String fileName) throws IOException {
        this.fileName = fileName;
        fileCount = 0;
        entryCount = 0;

        newFile(false);
        csvWriter.writeNext(HEADER);
    }

    public void exportGame(GameHistory gameHistory) throws IOException {
        if (entryCount > ENTRIES_PER_FILE) {
            newFile(false);
        }

        boolean AIWon = gameHistory.hasWon();
        for (SimpleGameState gameState : gameHistory.getGameStates()) {
            csvWriter.writeNext(gameStateToEntry(gameState, AIWon));
            entryCount++;
        }
    }

    public void writeSummary(long timeElapsed, int gamesPlayed, int aiWins, int lowestWins, int iterations) throws IOException {
        newFile(true);
        csvWriter.writeNext(new String[]{"AVERAGE_GAME_TIME_SECS", "TIME_ELAPSED_SECS", "TIME_ELAPSED_MINUTES", "TIME_ELAPSED_HOURS", "TOTAL_GAMES", "AI_WINS", "LOWEST_WINS", "AI_ITERATIONS"});
        csvWriter.writeNext(new String[]{gamesPlayed > 0 ? String.valueOf(timeElapsed / CastleConstants.SECOND_TO_MS / gamesPlayed) : "0",
                String.valueOf(timeElapsed / CastleConstants.SECOND_TO_MS),
                String.valueOf(timeElapsed / CastleConstants.MINUTE_TO_MS),
                String.valueOf(timeElapsed / CastleConstants.HOUR_TO_MS),
                String.valueOf(gamesPlayed),
                String.valueOf(aiWins),
                String.valueOf(lowestWins),
                String.valueOf(iterations)});
    }

    public void close() throws IOException {
        csvWriter.close();
    }

    private String[] gameStateToEntry(SimpleGameState gameState, boolean won) {
        String hand = cardsToString(gameState.getHand());
        String castleFU = cardsToString(gameState.getCastleFU());
        String castleFDSize = String.valueOf(gameState.getCastleFDSize());
        String opHandSize = String.valueOf(gameState.getOpHandSize());
        String opCastleFU = cardsToString(gameState.getOpCastleFU());
        String opCastleFDSize = String.valueOf(gameState.getOpCastleFDSize());
        String topCard = gameState.getTopCard() == null ? NO_CARDS : String.valueOf(gameState.getTopCard().getRank().getValueCode());
        String deckEmpty = gameState.isDeckEmpty() ? TRUE : FALSE;
        return new String[]{hand, castleFU, castleFDSize, opHandSize, opCastleFU, opCastleFDSize, topCard, deckEmpty, won ? TRUE : FALSE};
    }

    private String cardsToString(CardCollection cards) {
        if (cards.isEmpty()) {
            return NO_CARDS;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards.getCardCollection()) {
            stringBuilder.append(card.getRank().getValueCode()).append(LIST_DELIMITER);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private void newFile(boolean summaryFile) throws IOException {
        if (csvWriter != null) {
            csvWriter.close();
        }

        System.out.println("File Count: " + fileCount);
        System.out.println("Entry Count: " + entryCount);
        System.out.println("New file created.");

        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
        Writer writer = Files.newBufferedWriter(Paths.get(OUTPUT_FOLDER + (summaryFile ? SUMMARY_FILE_NAME : fileName + fileCount) + CSV_EXT));

        csvWriter = new CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        fileCount++;
    }
}
