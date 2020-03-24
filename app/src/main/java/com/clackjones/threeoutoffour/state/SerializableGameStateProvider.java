package com.clackjones.threeoutoffour.state;

import android.content.Context;

import com.clackjones.threeoutoffour.model.GameState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableGameStateProvider implements GameStateProvider {
    private static SerializableGameStateProvider gameStateProvider = null;
    private static String FILE_NAME = "GAME_STATE";

    private SerializableGameStateProvider() {
    }

    public static SerializableGameStateProvider getInstance() {
        if (gameStateProvider == null) {
            gameStateProvider = new SerializableGameStateProvider();
        }

        return gameStateProvider;
    }

    @Override
    public void saveGameState(GameState gameState, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameState);
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameState loadGameStateOrCreateNew(Context context) {
        File saveStateFile = new File(context.getFilesDir(), FILE_NAME);
        boolean isGameStatePersisted = saveStateFile.exists();

        if (! isGameStatePersisted) {
            return new GameState();
        } else {
            try {
                FileInputStream fis = new FileInputStream(saveStateFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                return (GameState) ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return new GameState();
    }

    @Override
    public void deleteGame(Context context) {
        File savedStateFile = new File(context.getFilesDir(), FILE_NAME);
        if (savedStateFile.exists()) {
            savedStateFile.delete();
        }
    }
}
