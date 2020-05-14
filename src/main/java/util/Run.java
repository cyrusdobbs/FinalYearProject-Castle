package util;

import controller.TerminalGameController;
import controller.players.HumanPlayerController;
import controller.players.PlayerController;
import model.GameHistory;
import model.GameState;
import model.PlayerModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import util.sqlexporter.SQLExporter;
import view.GameView;
import view.HumanGameView;
import view.TextGameView;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static util.SetupUtil.getConfiguration;

public class Run {

    private static Config config;

    private static final String FILE_NAME_PREFIX = "ISMCTS_";
    static final int MAX_ITERATIONS = 3200;
    private static final int GAMES_PER_EXPORT = 10;

    private static Random random = new Random();

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

    public static long evalTime = 0;

    public static void main(String[] args) throws IOException {
        // Start application
        logger.info("Application started.");

        // Get configuration
        config = getConfiguration(args);

        // Initialise exporters
        CSVExporter = new CSVExporter(FILE_NAME_PREFIX);
        if (config.isUploadToDatabase()) {
            SQLExporter = new SQLExporter();
        }

        // Run games
        System.out.println();
        if (config.getEndConditionType().equals(CastleConstants.TIME) || config.getEndConditionType().equals(CastleConstants.INDEFINITE)) {
            if (config.getEndConditionValue() != 0) {
                runUntilTime(config.getEndConditionValue());
            } else {
                run();
            }
        } else if (config.getEndConditionType().equals(CastleConstants.GAMES)) {
            runUntilNoOfGames(config.getEndConditionValue());
        }

        // Export games
        if (config.isUploadToDatabase()) {
            CSVExporter.writeSummary(timeElapsed, failedUpload_gamesPlayed, failedUpload_aiWins, failedUpload_lowestWins, MAX_ITERATIONS);
            logger.info("Failed SQL upload summary written.");
        } else if (config.isExportToCSV()) {
            CSVExporter.writeSummary(timeElapsed, gamesPlayed, trackedWins, otherWins, MAX_ITERATIONS);
            logger.info("Summary written.");
        }

        // End application
        CSVExporter.close();

        logger.info("Application finished.");
        System.out.println("Took: " + (timeElapsed / 1000) + " seconds.");
        System.out.println("Evaluation took: " + (evalTime / 1000) + " seconds.");
    }


    private static void run() throws IOException {
        List<GameHistory> gameHistories = new ArrayList<>();

        boolean running = true;
        while (running) {

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

            // Run game
            GameHistory gameHistory = runGame();
            gameHistories.add(gameHistory);
            gamesPlayed++;

            // Export
            if (gamesPlayed % GAMES_PER_EXPORT == 0) {
                export(gameHistories);
                gameHistories.clear();
            }
        }

        // Export excess
        if (!gameHistories.isEmpty()) {
            export(gameHistories);
        }

        timeElapsed = endCondition * CastleConstants.MINUTE_TO_MS;
    }

    private static void runUntilNoOfGames(int endCondition) throws IOException {
        long startTime = System.currentTimeMillis();

        List<GameHistory> gameHistories = new ArrayList<>();
        for (int i = 0; i < endCondition; i++) {

            // Run game
            GameHistory gameHistory = runGame();
            gameHistories.add(gameHistory);
            gamesPlayed++;

            // Export
            if (gamesPlayed % GAMES_PER_EXPORT == 0) {
                export(gameHistories);
                gameHistories.clear();
            }
        }

        // Export excess
        if (!gameHistories.isEmpty()) {
            export(gameHistories);
        }

        timeElapsed = System.currentTimeMillis() - startTime;
    }

    private static GameHistory runGame() {
        logger.info("New game.");
        TerminalGameController game = getNewGame();
        GameHistory gameHistory = game.run(config.isPrint());
        logger.info("Game finished.");

        // Update wins tally
        if (gameHistory.hasWon()) {
            trackedWins++;
        } else {
            otherWins++;
        }

        return gameHistory;
    }

    private static TerminalGameController getNewGame() {
        // Player controllers and models for this game
        List<PlayerController> newPlayerControllers = new ArrayList<>();
        List<PlayerModel> newPlayerModels = new ArrayList<>();
        int index = 0;
        for (PlayerController playerController : config.getPlayerControllers()) {
            newPlayerControllers.add(playerController.copy());
            newPlayerModels.add(newPlayerControllers.get(index).getPlayerModel());
            index++;
        }

        // Game view
        GameView gameView = containsInstance(newPlayerControllers, HumanPlayerController.class) ? new HumanGameView() : new TextGameView();

        // Tracked player to export (0 by default)
        int trackedPlayer = 0;

        // Randomise player positions
        if (config.isAlternateFirstPlayer()) {
            if (random.nextBoolean()) {
                logger.info("Player1: " + newPlayerModels.get(0).getName() + " | Player2: " + newPlayerModels.get(1).getName() + ".");
            } else {
                logger.info("Player1: " + newPlayerModels.get(1).getName() + " | Player2: " + newPlayerModels.get(0).getName() + ".");

                // Swap player numbers
                newPlayerControllers.get(1).setPlayerNo(0);
                newPlayerControllers.get(0).setPlayerNo(1);

                // Swap player positions
                Collections.swap(newPlayerControllers, 0, 1);
                Collections.swap(newPlayerModels, 0, 1);

                // Tracked player moved position
                trackedPlayer  = 1;
            }
        }

        // Return game
        return new TerminalGameController(new GameState(newPlayerModels), gameView, newPlayerControllers, trackedPlayer);
    }

    private static void export(List<GameHistory> gameHistories) throws IOException {
        logger.info("Attempting to export.");
        if (config.isUploadToDatabase()) {

            try {
                // Try to upload to database

                SQLExporter.updateDatabase(gameHistories);
                System.out.println(getCurrentTime() + ": Successfully uploaded " + gameHistories.size() + " games to the database.");
                logger.info("Games exported to DB.");

            } catch (SQLException e) {
                // Output to CSV if failed

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

        } else if (config.isExportToCSV()) {
            // Output to CSV
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

    // https://stackoverflow.com/questions/16681786/why-doesnt-arraylist-containsobject-class-work-for-finding-instances-types
    public static <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
        return list.stream().anyMatch(clazz::isInstance);
    }
}
