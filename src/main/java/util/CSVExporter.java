package util;

import com.opencsv.CSVWriter;
import model.GameHistory;
import model.SimpleGameState;
import model.cards.CardCollection;
import model.cards.card.Card;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class CSVExporter {

    // 50,000 is for 30 minutes of games
    private static final int ENTRIES_PER_FILE = 2500;
    private static final String OUTPUT_FOLDER = "output/";
    private static final String[] HEADER = {"HAND", "CASTLE_FU", "CASTLE_FD_SIZE", "OP_HAND_SIZE", "OP_CASTLE_FU", "OP_CASTLE_FD_SIZE", "TOP", "DECK_EMPTY", "WON"};
    private static final String NA = "NA";
    private static final String SUMMARY_FILE_NAME = "Summary";
    private static final String CSV_EXT = ".csv";

    private CSVWriter csvWriter;

    private String fileName;
    private int fileCount;
    private int totalEntryCount;
    private int currentEntryCount;

    CSVExporter(String fileName) {
        this.fileName = fileName;
        fileCount = 0;
        totalEntryCount = 0;
        currentEntryCount = 0;
    }

    void exportGames(List<GameHistory> gameHistories) throws IOException {
        for (GameHistory gameHistory : gameHistories) {
            exportGame(gameHistory);
        }
    }

    void exportGame(GameHistory gameHistory) throws IOException {
        if (fileCount == 0) {
            // Initiate file
            newFile(false);
            csvWriter.writeNext(HEADER);
        } else if (currentEntryCount > ENTRIES_PER_FILE) {
            newFile(false);
        }

        boolean AIWon = gameHistory.hasWon();
        for (SimpleGameState gameState : gameHistory.getGameStates()) {
            csvWriter.writeNext(gameStateToEntry(gameState, AIWon));
            currentEntryCount++;
        }
    }

    void writeSummary(long timeElapsed, int gamesPlayed, int aiWins, int lowestWins, int iterations) throws IOException {
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

    void close() throws IOException {
        csvWriter.close();
    }

    private String[] gameStateToEntry(SimpleGameState gameState, boolean won) {
        String hand = cardsToString(gameState.getHand());
        String castleFU = cardsToString(gameState.getCastleFU());
        String castleFDSize = String.valueOf(gameState.getCastleFDSize());
        String opHandSize = String.valueOf(gameState.getOpHandSize());
        String opCastleFU = cardsToString(gameState.getOpCastleFU());
        String opCastleFDSize = String.valueOf(gameState.getOpCastleFDSize());
        String topCard = gameState.getTopCard() == null ? CastleConstants.NO_CARDS : String.valueOf(gameState.getTopCard().getRank().getValueCode());
        String deckEmpty = gameState.isDeckEmpty() ? CastleConstants.TRUE : CastleConstants.FALSE;
        return new String[]{hand, castleFU, castleFDSize, opHandSize, opCastleFU, opCastleFDSize, topCard, deckEmpty, won ? CastleConstants.TRUE : CastleConstants.FALSE};
    }

    private String cardsToString(CardCollection cards) {
        if (cards.isEmpty()) {
            return CastleConstants.NO_CARDS;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : cards.getCardCollection()) {
            stringBuilder.append(card.getRank().getValueCode()).append(CastleConstants.LIST_DELIMITER);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private void newFile(boolean summaryFile) throws IOException {
        if (csvWriter != null) {
            csvWriter.close();
        }

        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
        Writer writer = Files.newBufferedWriter(Paths.get(OUTPUT_FOLDER + (summaryFile ? SUMMARY_FILE_NAME : fileName + fileCount) + CSV_EXT));

        csvWriter = new CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        fileCount++;
        totalEntryCount += currentEntryCount;
        currentEntryCount = 0;

        System.out.println("--------------" + getCurrentTime() + "--------------");
        if (!summaryFile) {
            System.out.println(getCurrentTime() + ": New entry file created. " + fileCount + " files containing " + totalEntryCount + " entries.");
        } else {
            System.out.println("Summary file created.");
        }
    }

    private String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();
        return df.format(date);
    }
}
