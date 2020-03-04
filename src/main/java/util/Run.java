package util;

import controller.TerminalGameController;
import controller.players.AIController;
import controller.players.IsmctsPlayerController;
import controller.players.LowestPlayerController;
import model.GameHistory;
import model.GameState;
import model.Player;
import sqlexporter.SQLExporter;
import view.TextGameView;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static util.CastleConstants.ISMCTS;
import static util.CastleConstants.LOWEST;

public class Run {

    private static final String TIME = "T";
    private static final String GAMES = "G";
    private static final String YES = "Y";
    private static final String FILE_NAME_PREFIX = "ISMCTS_";
    private static final int MAX_ITERATIONS = 3200;
    private static final int GAMES_PER_EXPORT = 10;

    private static Random random = new Random();

    private static boolean print;
    private static boolean uploadToDatabase;

    private static CSVExporter CSVExporter;
    private static SQLExporter SQLExporter;
    private static long timeElapsed;

    private static int gamesPlayed;
    private static int aiWins;
    private static int lowestWins;

    private static int failedUpload_gamesPlayed;
    private static int failedUpload_aiWins;
    private static int failedUpload_lowestWins;

    public static void main(String[] args) throws IOException {
        String endConditionType = args[0];
        int endCondition = Integer.parseInt(args[1]);
        print = args[2].equals(YES);
        uploadToDatabase = args[3].equals(YES);
        CSVExporter = new CSVExporter(FILE_NAME_PREFIX);
        if (uploadToDatabase) {
            SQLExporter = new SQLExporter();
        }

        if (endConditionType.equals(TIME)) {
            runUntilTime(endCondition);
        } else if (endConditionType.equals(GAMES)) {
            runUntilNoOfGames(endCondition);
        }

        if (uploadToDatabase) {
            CSVExporter.writeSummary(timeElapsed, failedUpload_gamesPlayed, failedUpload_aiWins, failedUpload_lowestWins, MAX_ITERATIONS);
        } else {
            CSVExporter.writeSummary(timeElapsed, gamesPlayed, aiWins, lowestWins, MAX_ITERATIONS);
        }
        CSVExporter.close();
    }

    private static void runUntilNoOfGames(int endCondition) throws IOException {
        long startTime = System.currentTimeMillis();

        List<GameHistory> gameHistories = new ArrayList<>();
        for (int i = 0; i < endCondition; i++) {

            GameHistory gameHistory = runGame();
            gamesPlayed++;
            gameHistories.add(gameHistory);

            if (gamesPlayed % GAMES_PER_EXPORT == 0) {
                export(gameHistories);
            }
        }

        if (!gameHistories.isEmpty()) {
            export(gameHistories);
        }

        timeElapsed = System.currentTimeMillis() - startTime;
    }

    private static void runUntilTime(int endCondition) throws IOException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (endCondition * CastleConstants.MINUTE_TO_MS);

        List<GameHistory> gameHistories = new ArrayList<>();
        while (System.currentTimeMillis() < endTime) {

            GameHistory gameHistory = runGame();
            gamesPlayed++;
            gameHistories.add(gameHistory);

            if (gamesPlayed % GAMES_PER_EXPORT == 0) {
                export(gameHistories);
                gameHistories.clear();
            }
        }

        if (!gameHistories.isEmpty()) {
            export(gameHistories);
        }

        timeElapsed = endCondition * CastleConstants.MINUTE_TO_MS;
    }

    private static GameHistory runGame() {
        TerminalGameController game = getNewGame();
        GameHistory gameHistory = game.run(print);

        if (gameHistory.hasWon()) {
            aiWins++;
        } else {
            lowestWins++;
        }
        return gameHistory;
    }

    private static TerminalGameController getNewGame() {
        List<Player> playerModels = new ArrayList<>();
        playerModels.add(new Player(ISMCTS));
        playerModels.add(new Player(LOWEST));

        List<AIController> players = new ArrayList<>();

        players.add(new IsmctsPlayerController(playerModels.get(0), MAX_ITERATIONS, false, print));
        players.add(new LowestPlayerController(playerModels.get(1)));

//        if (random.nextBoolean()) {
//            players.add(new IsmctsPlayerController(playerModels.get(0), 2500, false, true));
//            players.add(new LowestPlayerController(playerModels.get(1)));
//        } else {
//            players.add(new LowestPlayerController(playerModels.get(1)));
//            players.add(new IsmctsPlayerController(playerModels.get(0), 2500, false, true));
//        }

        return new TerminalGameController(new GameState(playerModels), new TextGameView(), players);
    }

    private static void export(List<GameHistory> gameHistories) throws IOException {
        if (uploadToDatabase) {
            try {
                SQLExporter.updateDatabase(gameHistories);
                System.out.println(getCurrentTime() + ": Successfully uploaded " + gameHistories.size() + " games to the database.");
            } catch (SQLException e) {
                int noOfAiWins = countAiWins(gameHistories);
                String time = getCurrentTime();
                System.out.println(time + ": SQLException: " + e.getMessage());
                System.out.println(time + ": Exported as CSV instead.");
                // Export to CSV if DB fails
                CSVExporter.exportGames(gameHistories);
                failedUpload_gamesPlayed += gameHistories.size();
                failedUpload_aiWins += noOfAiWins;
                failedUpload_lowestWins += gameHistories.size() - noOfAiWins;
            }
        } else {
            CSVExporter.exportGames(gameHistories);
        }
    }

    private static int countAiWins(List<GameHistory> gameHistories) {
        int aiWins = 0;
        for (GameHistory gameHistory : gameHistories) {
            if (gameHistory.hasWon()) {
                aiWins++;
            }
        }
        return aiWins;
    }

    private static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();
        return df.format(date);
    }
}
