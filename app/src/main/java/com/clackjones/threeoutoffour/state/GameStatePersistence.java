package com.clackjones.threeoutoffour.state;

import com.clackjones.threeoutoffour.model.GameState;

interface GameStatePersistence {
    void saveGameState(GameState gameState);
    GameState loadGameState();
}
