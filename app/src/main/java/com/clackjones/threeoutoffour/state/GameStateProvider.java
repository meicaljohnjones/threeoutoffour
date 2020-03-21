package com.clackjones.threeoutoffour.state;

import com.clackjones.threeoutoffour.model.GameState;

public interface GameStateProvider {
    void saveGameState(GameState gameState);
    GameState loadGameStateOrCreateNew();
}
