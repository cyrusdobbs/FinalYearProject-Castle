package model;

import java.util.List;

public class Game {

    private List<SimpleGameState> gameStates;
    private boolean won;

    public Game(List<SimpleGameState> gameStates, boolean won) {
        this.gameStates = gameStates;
        this.won = won;
    }

    public List<SimpleGameState> getGameStates() {
        return gameStates;
    }

    public boolean hasWon() {
        return won;
    }
}
