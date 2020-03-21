package com.clackjones.threeoutoffour.state;

import com.clackjones.threeoutoffour.model.GameState;

public class SerializableGameStateProvider implements GameStateProvider {
    private static SerializableGameStateProvider gameStateProvider = null;

    private SerializableGameStateProvider() {
    }

    public static SerializableGameStateProvider getInstance() {
        if (gameStateProvider == null) {
            gameStateProvider = new SerializableGameStateProvider();
        }

        return gameStateProvider;
    }

    @Override
    public void saveGameState(GameState gameState) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public GameState loadGameStateOrCreateNew() {
        // TODO also try and load from file - for now, we will just create a new GameState
        return new GameState();
    }
}
