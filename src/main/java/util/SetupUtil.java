package util;

import controller.players.HumanPlayerController;
import controller.players.LowestPlayerController;
import controller.players.PlayerController;
import controller.players.RandomPlayer;
import controller.players.ismcts.EvaluatingIsmctsPlayerController;
import controller.players.ismcts.Evaluator;
import controller.players.ismcts.StandardIsmctsPlayerController;
import model.PlayerModel;
import util.terminal.InputConstants;
import util.terminal.TerminalUtil;

import java.util.ArrayList;
import java.util.List;

public class SetupUtil {

    public static void main(String[] args) {
        getConfiguration();
    }

    public static Config getConfiguration(String[] args) {
        if (args.length == 0) {
            return getConfiguration();
        } else if (args.length != 4) {
            println("Invalid number of arguments.");
            println("   - Use 0 args for full terminal setup");
            println("      OR ");
            println("   - Use 4 args [End Condition type (G|T), End Condition (Integer|0), Print Games (Y|N), Export to DB (Y|N)]");
            System.exit(0);
        }

        Config config = new Config();

        config.setEndConditionType(args[0]);
        config.setEndConditionValue(Integer.parseInt(args[1]));
        config.setPrint(args[2].equals(InputConstants.YES));
        config.setUploadToDatabase(args[3].equals(InputConstants.YES));
        config.setExportToCSV(false);

        List<PlayerController> playerControllers = new ArrayList<>();
        playerControllers.add(new StandardIsmctsPlayerController(new PlayerModel(CastleConstants.ISMCTS), 0, Run.MAX_ITERATIONS, false, config.isPrint()));
        playerControllers.add(new LowestPlayerController(new PlayerModel(CastleConstants.LOWEST), 1));

        config.setPlayerControllers(playerControllers);
        config.setAlternateFirstPlayer(true);
        return config;
    }

    public static Config getConfiguration() {
        Config config = new Config();

        config.setEndConditionType(getEndConditionType());
        if (config.getEndConditionType().equals(CastleConstants.INDEFINITE)) {
            config.setEndConditionValue(0);
        } else {
            config.setEndConditionValue(getEndConditionValue(config.getEndConditionType()));
        }
        config.setPrint(getPrint());
        config.setExportToCSV(getExportToCSV());
        config.setUploadToDatabase(getUploadToDatabase());
        int playerCount = getPlayerCount();

        List<PlayerController> playerControllers = new ArrayList<>();
        for (int player = 0; player < playerCount; player++) {
            playerControllers.add(getPlayer(player, config.isPrint()));
        }

        config.setPlayerControllers(playerControllers);
        config.setAlternateFirstPlayer(playerCount == 2 && getAlternateFirstPlayer());
        return config;
    }

    private static int getPlayerCount() {
        println("How many players: (2-4)");
        return TerminalUtil.getParsedIntegerInput(InputConstants.TWO_FOUR_REGEX);
    }

    private static boolean getAlternateFirstPlayer() {
        println("Alternate which player goes first? ");
        println("   1. Yes");
        println("   2. No");
        int userInput = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_TWO_REGEX);
        return userInput == 1;
    }

    private static PlayerController getPlayer(int playerNo, boolean print) {
        if (playerNo == 0) {
            println("(Statistic Tracked) Player" + (playerNo + 1) + " type: ");
        } else {
            println("Player" + (playerNo + 1) + " type: ");
        }

        println("   1. Human");
        println("   2. ISMCTS");
        println("   3. Custom Evaluation ISMCTS");
        println("   4. Lowest card strategy");
        println("   5. Random decisions");
        int userInput = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_FIVE_REGEX);

        boolean printChoice = false;
        switch (userInput) {
            case 1:
                return new HumanPlayerController(new PlayerModel(CastleConstants.HUMAN), playerNo);
            case 2:
                if (print) {
                    println("Print ISMCTS W/V/A? ");
                    println("   1. Yes");
                    println("   2. No");
                    printChoice = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_TWO_REGEX) == 1;
                }
                return new StandardIsmctsPlayerController(new PlayerModel(CastleConstants.ISMCTS), playerNo, Run.MAX_ITERATIONS, false, printChoice);
            case 3:
                // Was using 40
                println("Enter simulation depth limit: ");
                String depth = TerminalUtil.getParsedInput(InputConstants.INTEGER_REGEX);
                if (print) {
                    println("Print ISMCTS W/V/A? ");
                    println("   1. Yes");
                    println("   2. No");
                    printChoice = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_TWO_REGEX) == 1;
                }
                return new EvaluatingIsmctsPlayerController(new PlayerModel(CastleConstants.E_ISMCTS), playerNo,
                        Run.MAX_ITERATIONS, false, printChoice, new Evaluator(CastleConstants.MODEL_NAME), Integer.parseInt(depth));
            case 4:
                return new LowestPlayerController(new PlayerModel(CastleConstants.LOWEST), playerNo);
            case 5:
                return new RandomPlayer(new PlayerModel(CastleConstants.RANDOM), playerNo);
        }

        return null;
    }

    private static String getEndConditionType() {
        println("Play until:");
        println("   1. No. of games");
        println("   2. Time limit");
        println("   3. Indefinitely");
        int userInput = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_THREE_REGEX);
        switch (userInput) {
            case 1:
                return CastleConstants.GAMES;
            case 2:
                return CastleConstants.TIME;
            case 3:
                return CastleConstants.INDEFINITE;
            default:
                return null;
        }
    }

    private static int getEndConditionValue(String endConditionType) {
        if (endConditionType.equals(CastleConstants.GAMES)) {
            println("Enter the number of games to be played: ");
        } else if (endConditionType.equals(CastleConstants.TIME)) {
            println("Enter the number of minutes to be played: ");
        }
        return TerminalUtil.getParsedIntegerInput(InputConstants.INTEGER_REGEX);
    }

    private static boolean getPrint() {
        println("Print the game to console? ");
        println("   1. Yes");
        println("   2. No");
        int userInput = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_TWO_REGEX);
        return userInput == 1;
    }

    private static boolean getExportToCSV() {
        println("Export results to CSV files? ");
        println("   1. Yes");
        println("   2. No");
        int userInput = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_TWO_REGEX);
        return userInput == 1;
    }

    private static boolean getUploadToDatabase() {
        println("Export results to database? ");
        println("   1. Yes");
        println("   2. No");
        int userInput = TerminalUtil.getParsedIntegerInput(InputConstants.ONE_TWO_REGEX);
        return userInput == 1;
    }

    private static void println(String string) {
        System.out.println(string);
    }
}
