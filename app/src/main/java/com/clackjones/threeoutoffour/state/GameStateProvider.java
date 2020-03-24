package com.clackjones.threeoutoffour.state;

import android.content.Context;

import com.clackjones.threeoutoffour.model.GameState;

public interface GameStateProvider {
    void saveGameState(GameState gameState, Context context);
    GameState loadGameStateOrCreateNew(Context context);
    void deleteGame(Context context);
}
