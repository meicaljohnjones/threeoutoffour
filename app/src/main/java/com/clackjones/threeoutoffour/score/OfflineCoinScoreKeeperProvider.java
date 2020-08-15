package com.clackjones.threeoutoffour.score;

import android.content.Context;

import com.clackjones.threeoutoffour.model.GameState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OfflineCoinScoreKeeperProvider {
    private static final String FILE_NAME = "OFFLINE_COIN_SCORE_KEEPER";

    private static OfflineCoinScoreKeeperProvider offlineCoinScoreKeeperProvider = null;

    private OfflineCoinScoreKeeperProvider() {
    }

    public static OfflineCoinScoreKeeperProvider getInstance() {
        if (offlineCoinScoreKeeperProvider == null) {
            offlineCoinScoreKeeperProvider = new OfflineCoinScoreKeeperProvider();
        }

        return offlineCoinScoreKeeperProvider;
    }

    public OfflineCoinScoreKeeper loadOrCreate(Context context) {
        File saveStateFile = new File(context.getFilesDir(), FILE_NAME);
        boolean isGameStatePersisted = saveStateFile.exists();

        OfflineCoinScoreKeeper offlineCoinScoreKeeper =  null;
        if (isGameStatePersisted) {
            try {
                FileInputStream fis = new FileInputStream(saveStateFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                offlineCoinScoreKeeper = (OfflineCoinScoreKeeper) ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            offlineCoinScoreKeeper = new OfflineCoinScoreKeeper();
        }

        offlineCoinScoreKeeper.setApplicationContext(context);
        return offlineCoinScoreKeeper;
    }

    public void saveCoinScoreKeeper(Context context, OfflineCoinScoreKeeper offlineCoinScoreKeeper) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(offlineCoinScoreKeeper);
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
