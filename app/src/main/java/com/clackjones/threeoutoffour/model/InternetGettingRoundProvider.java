package com.clackjones.threeoutoffour.model;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InternetGettingRoundProvider implements RoundProvider {
    private static InternetGettingRoundProvider roundProvider;
    private final RoundsDeserializer roundsDeserializer;
    private final SerializedRoundResourceGetter serializedRoundResourceGetter;
    private Context context;
    private Map<Integer, Round> rounds;
    private ExecutorService executorService;



    public InternetGettingRoundProvider(Context context,
                                        SerializedRoundResourceGetter serializedRoundResourceGetter,
                                        RoundsDeserializer roundsDeserializer) {
        this.context = context;
        this.serializedRoundResourceGetter = serializedRoundResourceGetter;
        this.roundsDeserializer = roundsDeserializer;
        this.rounds = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public static InternetGettingRoundProvider getInstance(Context context) {
        if (InternetGettingRoundProvider.roundProvider == null) {
            InternetGettingRoundProvider.roundProvider =
                    new InternetGettingRoundProvider(
                            context,
                            new HttpSerializedRoundResourceGetter(),
                            new RoundsDeserializer()
                    );
            InternetGettingRoundProvider.roundProvider.initialize();
        }
        return InternetGettingRoundProvider.roundProvider;
    }

    private void initialize() {
    }

    @Override
    public Round getNextRound(int lastRoundNumber) {
        int nextRoundIdx = lastRoundNumber + 1;

        // 1. try and load from memory
        if (isRoundLoaded(nextRoundIdx)) {
            return this.rounds.get(nextRoundIdx);
        }

        // 2. try and load from serialized file
        loadRoundsFromFile();
        if (isRoundLoaded(nextRoundIdx)) {
            return this.rounds.get(nextRoundIdx);
        }

        // 3. now try and load from internet
        loadNextRoundsFromResource(nextRoundIdx);
        saveRoundsToFile();

        if (isRoundLoaded(nextRoundIdx)) {
            return this.rounds.get(nextRoundIdx);
        }

        // 4. give up, return null
        return null;
    }


    private boolean isRoundLoaded(int nextRoundIdx) {
        return this.rounds.containsKey(nextRoundIdx);
    }

    private void loadNextRoundsFromResource(final Integer nextRoundIdx) {
        Callable<Map<Integer, Round>> callableLoadRounds = new Callable<Map<Integer, Round>>() {

            @Override
            public Map<Integer, Round> call() throws Exception {
                return loadNextRoundsFromResourceImpl(nextRoundIdx);
            }
        };

        Future<Map<Integer, Round>> futureRounds = this.executorService.submit(callableLoadRounds);
        while(!futureRounds.isDone()) {}

        try {
            this.rounds = futureRounds.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Map<Integer, Round> loadNextRoundsFromResourceImpl(Integer nextRoundIdx) {
        try {
            InputStream roundsInputStream = null;
            try {
                roundsInputStream = serializedRoundResourceGetter.getResourceContainingRound(nextRoundIdx);
                return roundsDeserializer.readRounds(roundsInputStream);
            } finally {
                if (roundsInputStream != null) {
                    roundsInputStream.close();
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        return null;
    }

    private void saveRoundsToFile() {
        FileOutputStream privateAppFileOutputStream = null;
        try {
            try {
                privateAppFileOutputStream = context.openFileOutput("current_rounds", Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(privateAppFileOutputStream);
                oos.writeObject(this.rounds);
            } finally {
                privateAppFileOutputStream.close();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void loadRoundsFromFile() {
        FileInputStream privateAppFileInputStream = null;
        try {

                File currentRoundsFile = context.getFileStreamPath("current_rounds");
                if (!currentRoundsFile.exists()) {
                    return;
                }

                privateAppFileInputStream = context.openFileInput("current_rounds");
                ObjectInputStream objectInputStream = new ObjectInputStream(privateAppFileInputStream);
                Map<Integer, Round> loadedRounds = (Map<Integer, Round>) objectInputStream.readObject();
                this.rounds = loadedRounds;
        } catch (IOException | ClassNotFoundException io) {
            io.printStackTrace();
        } finally {
            try {
                if (privateAppFileInputStream != null) privateAppFileInputStream.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

}
