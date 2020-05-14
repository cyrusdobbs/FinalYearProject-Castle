package view;

import model.GameState;

public interface GameView {

    void updateView(GameState gameState);

    void startView(GameState gameState);
}
