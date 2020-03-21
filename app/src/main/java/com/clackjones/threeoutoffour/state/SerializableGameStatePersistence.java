package com.clackjones.threeoutoffour.state;

import com.clackjones.threeoutoffour.model.GameState;

public class SerializableGameStatePersistence implements GameStatePersistence {
    private static SerializableGameStatePersistence gameStatePersistence;

    private SerializableGameStatePersistence() {

    }

    public SerializableGameStatePersistence getInstance() {
        return new SerializableGameStatePersistence();
    }

    @Override
    public void saveGameState(GameState gameState) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public GameState loadGameState() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
