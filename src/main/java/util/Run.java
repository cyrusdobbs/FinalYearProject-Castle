package util;

import controller.TerminalGameController;
import controller.players.AIController;
import controller.players.IsmctsPlayerController;
import controller.players.LowestPlayerController;
import model.GameHistory;
import model.GameState;
import model.Player;
import view.TextGameView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static util.CastleConstants.ISMCTS;
import static util.CastleConstants.LOWEST;

public class Run {

    private static final String TIME = "T";
    private static final String GAMES = "G";
    private static final String YES = "Y";
    public static final String FILE_NAME_PREFIX = "ISMCTS_";
    public static final int MAX_ITERATIONS = 3200;

    private static Random random = new Random();

    private static boolean print;
    private static boolean exportToCsv;

    private static Exporter exporter;
    private static long timeElapsed;
    private static int gamesPlayed;
    private static int AIWins;
    private static int LowestWins;

    public static void main(String[] args) throws IOException {
        String endConditionType = args[0];
        int endCondition = Integer.parseInt(args[1]);
        print = args[2].equals(YES);
        exportToCsv = args[3].equals(YES);
        exporter = new Exporter(FILE_NAME_PREFIX);

        if (endConditionType.equals(TIME)) {
            runUntilTime(endCondition);
        } else if (endConditionType.equals(GAMES)) {
            runUntilNoOfGames(endCondition);
        }

        exporter.writeSummary(timeElapsed, gamesPlayed, AIWins, LowestWins, MAX_ITERATIONS);
        exporter.close();
    }

    private static void runUntilNoOfGames(int endCondition) throws IOException {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < endCondition; i++) {
            runGame();
        }

        timeElapsed = System.currentTimeMillis() - startTime;
        gamesPlayed = endCondition;
    }

    private static void runUntilTime(int endCondition) throws IOException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (endCondition * CastleConstants.MINUTE_TO_MS);
        gamesPlayed = 0;

        while (System.currentTimeMillis() < endTime) {
            runGame();
            gamesPlayed++;
        }

        timeElapsed = endCondition * CastleConstants.MINUTE_TO_MS;
    }

    private static void runGame() throws IOException {
        TerminalGameController game = getNewGame();
        GameHistory gameHistory = game.run(print);
        if (exportToCsv) {
            exporter.exportGame(gameHistory);
            if (gameHistory.hasWon()) {
                AIWins++;
            } else {
                LowestWins++;
            }
        }
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
}
