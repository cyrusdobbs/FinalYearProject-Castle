package util;

import controller.TerminalGameController;
import controller.players.AIController;
import controller.players.LowestPlayerController;
import controller.players.ismcts.EvaluatingIsmctsPlayerController;
import controller.players.ismcts.StandardIsmctsPlayerController;
import model.GameHistory;
import model.GameState;
import model.Player;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import util.sqlexporter.SQLExporter;
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
    private static int trackedWins;
    private static int otherWins;

    private static int failedUpload_gamesPlayed;
    private static int failedUpload_aiWins;
    private static int failedUpload_lowestWins;

    private static Logger logger = LogManager.getLogger(Run.class);

    public static void main(String[] args) throws IOException {
        logger.info("Application started.");
        String endConditionType = args[0];
        int endCondition = Integer.parseInt(args[1]);
        print = args[2].equals(YES);
        uploadToDatabase = args[3].equals(YES);
        CSVExporter = new CSVExporter(FILE_NAME_PREFIX);

        if (uploadToDatabase) {
            SQLExporter = new SQLExporter();
        }

        if (endConditionType.equals(TIME)) {
            if (endCondition != 0) {
                runUntilTime(endCondition);
            } else {
                run();
            }
        } else if (endConditionType.equals(GAMES)) {
            runUntilNoOfGames(endCondition);
        }

        if (uploadToDatabase) {
            CSVExporter.writeSummary(timeElapsed, failedUpload_gamesPlayed, failedUpload_aiWins, failedUpload_lowestWins, MAX_ITERATIONS);
            logger.info("Failed SQL upload summary written.");
        } else {
            CSVExporter.writeSummary(timeElapsed, gamesPlayed, trackedWins, otherWins, MAX_ITERATIONS);
            logger.info("Summary written.");
        }
        CSVExporter.close();
        logger.info("Application finished.");
    }

    private static void run() throws IOException {
        List<GameHistory> gameHistories = new ArrayList<>();
        while (true) {

            GameHistory gameHistory = runGame();
            gamesPlayed++;
            gameHistories.add(gameHistory);

            if (gamesPlayed % GAMES_PER_EXPORT == 0) {
                export(gameHistories);
                gameHistories.clear();
            }
        }
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

    private static void runUntilNoOfGames(int endCondition) throws IOException {
        long startTime = System.currentTimeMillis();

        List<GameHistory> gameHistories = new ArrayList<>();
        for (int i = 0; i < endCondition; i++) {

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

        timeElapsed = System.currentTimeMillis() - startTime;
    }

    private static GameHistory runGame() {
        logger.info("New game.");
        TerminalGameController game = getNewGame();
        GameHistory gameHistory = game.run(print);

        if (gameHistory.hasWon()) {
            trackedWins++;
        } else {
            otherWins++;
        }
        logger.info("Game finished.");
        return gameHistory;
    }

    private static TerminalGameController getNewGame() {
//        List<Player> playerModels = new ArrayList<>();

//        List<AIController> players = new ArrayList<>();
//        playerModels.add(new Player(ISMCTS));
//        playerModels.add(new Player(LOWEST));

//        int trackedPlayer = 0;
//        players.add(new EvaluatingIsmctsPlayerController(playerModels.get(0), 0, MAX_ITERATIONS, false, print, new Evaluator("convModel.h5"), 40));
//        players.add(new LowestPlayerController(playerModels.get(1), 1));
        //players.add(new StandardIsmctsPlayerController(playerModels.get(1), 0, MAX_ITERATIONS, false, print));

        List<Player> playerModels = new ArrayList<>();
        List<AIController> players = new ArrayList<>();
        int trackedPlayer;
        if (random.nextBoolean()) {
            logger.info("Player1: ISMCTS | Player2: Lowest.");
            trackedPlayer = 0;
            playerModels.add(new Player(ISMCTS));
            playerModels.add(new Player(LOWEST));
            players.add(new StandardIsmctsPlayerController(playerModels.get(0), 0, MAX_ITERATIONS, false, print));
            players.add(new LowestPlayerController(playerModels.get(1), 1));

        } else {
            logger.info("Player1: Lowest | Player2: ISMCTS.");
            trackedPlayer = 1;
            playerModels.add(new Player(LOWEST));
            playerModels.add(new Player(ISMCTS));
            players.add(new LowestPlayerController(playerModels.get(0), 0));
            players.add(new StandardIsmctsPlayerController(playerModels.get(1), 1, MAX_ITERATIONS, false, print));
        }

        return new TerminalGameController(new GameState(playerModels), new TextGameView(), players, trackedPlayer);
    }

    private static void export(List<GameHistory> gameHistories) throws IOException {
        logger.info("Attempting to export.");
        if (uploadToDatabase) {
            try {
                SQLExporter.updateDatabase(gameHistories);
                System.out.println(getCurrentTime() + ": Successfully uploaded " + gameHistories.size() + " games to the database.");
                logger.info("Games exported to DB.");
            } catch (SQLException e) {
                int wins = countTrackedWins(gameHistories);
                String time = getCurrentTime();
                System.out.println(time + ": SQLException: " + e.getMessage());
                System.out.println(time + ": Exported as CSV instead.");
                // Export to CSV if DB fails
                CSVExporter.exportGames(gameHistories);
                failedUpload_gamesPlayed += gameHistories.size();
                failedUpload_aiWins += wins;
                failedUpload_lowestWins += gameHistories.size() - wins;
                logger.info("DB export failed, exported to CSV.");
            }
        } else {
            CSVExporter.exportGames(gameHistories);
            logger.info("Games exported to CSV.");
        }
    }

    private static int countTrackedWins(List<GameHistory> gameHistories) {
        int wins = 0;
        for (GameHistory gameHistory : gameHistories) {
            if (gameHistory.hasWon()) {
                wins++;
            }
        }
        return wins;
    }

    private static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();
        return df.format(date);
    }
}
