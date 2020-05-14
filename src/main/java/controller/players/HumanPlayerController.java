package controller.players;

import model.GameState;
import model.PlayerModel;
import moves.CastleMove;
import util.terminal.InputConstants;
import util.terminal.TerminalUtil;

import java.util.List;

public class HumanPlayerController extends PlayerController {

    public HumanPlayerController(PlayerModel playerModel, int playerNo) {
        super(playerModel, playerNo);
    }

    @Override
    public CastleMove getMove(GameState gameState) {

        List<CastleMove> availableMoves = getMoves(gameState);

        println("Select a move:");
        int moveNo = 1;
        for (CastleMove castleMove : availableMoves) {
            println("   " + moveNo + ". " + castleMove.toHumanString());
            moveNo++;
        }

        int userInput = -1;
        boolean firstTry = true;
        while (userInput < 0 || userInput > availableMoves.size()) {
            if (!firstTry) {
                println( "That is not an option.");
            }
            userInput = TerminalUtil.getParsedIntegerInput(InputConstants.INTEGER_REGEX);
            firstTry = false;
        }
        return availableMoves.get(userInput - 1);
    }

    @Override
    public PlayerController copy() {
        return new HumanPlayerController(playerModel.copy(), playerNo);
    }

    private static void println(String string) {
        System.out.println(string);
    }
}
